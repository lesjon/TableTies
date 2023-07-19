package nl.leonklute.backend.service;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.domain.Person;
import nl.leonklute.backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeopleService {
    private final PersonRepository personRepository;

    @Value("classpath:people.csv")
    Resource defaultPeople;
    @Autowired
    public PeopleService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private List<Person> readPeopleFromFile(String peopleFileName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (var peopleFile = new BufferedReader(new FileReader(peopleFileName))) {
            while (peopleFile.ready()) {
                String nameLine = peopleFile.readLine();
                var person = new Person();
                person.setName(nameLine);
                people.add(person);
            }
        }
        return people;
    }

    public void loadDefaults(KeycloakUser keycloakUser) throws IOException {
        List<Person> people = readPeopleFromFile(defaultPeople.getFile().getAbsolutePath());
        for (var person : people) {
            person = personRepository.getByName(person.getName()).orElse(person);
            person.setName(person.getName());
            person.setKeycloakUser(keycloakUser);
            personRepository.save(person);
        }
    }

    public List<Person> getAllPeopleByUser(KeycloakUser keycloakUser) {
        return personRepository.findAllByKeycloakUser(keycloakUser);
    }
}
