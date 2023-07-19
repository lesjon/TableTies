package nl.leonklute.backend.service;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.domain.Person;
import nl.leonklute.backend.domain.Relation;
import nl.leonklute.backend.repository.PersonRepository;
import nl.leonklute.backend.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RelationService {

    private final RelationRepository relationRepository;
    private final PersonRepository personRepository;

    @Value("classpath:relations.csv")
    Resource defaultRelations;
    @Autowired
    public RelationService(RelationRepository relationRepository, PersonRepository personRepository) {
        this.relationRepository = relationRepository;
        this.personRepository = personRepository;
    }

    public void loadDefaults(KeycloakUser keycloakUser) throws IOException {
        List<Relation> relations = readRelationsFromFile(defaultRelations.getFile().getAbsolutePath());
        log.debug("Loaded {} from file", relations);
        List<Relation> dbRelations = relationRepository.findAllByKeycloakUser(keycloakUser);
        for (var relation : relations) {
            String name1 = relation.getPerson1().getName();
            String name2 = relation.getPerson2().getName();
            Optional<Relation> foundRelation = dbRelations.stream()
                    .filter(dbRelation -> dbRelation.getPerson1().getName().equals(name1))
                    .filter(dbRelation -> dbRelation.getPerson2().getName().equals(name2))
                    .findFirst();
            relation = foundRelation.orElse(relation);
            relation.setKeycloakUser(keycloakUser);
            relationRepository.save(relation);
        }
    }
    public List<Relation> readRelationsFromFile(String relationsFileName) throws IOException {
        List<Relation> relations = new ArrayList<>();
        try (var relationsFile = new BufferedReader(new FileReader(relationsFileName))) {
            while (relationsFile.ready()) {
                String relationLine = relationsFile.readLine();
                var relation = new Relation();
                String[] relationParts = relationLine.split(",\s*");
                Person person1 = personRepository.getByName(relationParts[0]).get();
                relation.setPerson1(person1);
                Person person2 = personRepository.getByName(relationParts[1]).get();
                relation.setPerson2(person2);
                relation.setRelationStrength(Double.parseDouble(relationParts[2]));
                relations.add(relation);
            }
        }
        return relations;
    }

    public List<Relation> getAllRelationsByUser(KeycloakUser keycloakUser) {
        return relationRepository.findAllByKeycloakUser(keycloakUser);
    }
}
