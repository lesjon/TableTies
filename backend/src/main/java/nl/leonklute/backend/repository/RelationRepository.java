package nl.leonklute.backend.repository;

import nl.leonklute.backend.domain.Event;
import nl.leonklute.backend.domain.Person;
import nl.leonklute.backend.domain.Relation;
import nl.leonklute.backend.domain.KeycloakUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Integer> {
    List<Relation> findAllByEvent(Event event);
    Optional<Relation> findByPerson1AndPerson2AndEvent(Person person1, Person person2, Event event);
}
