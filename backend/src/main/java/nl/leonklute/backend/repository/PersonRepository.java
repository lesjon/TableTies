package nl.leonklute.backend.repository;

import nl.leonklute.backend.domain.KeycloakUser;
import nl.leonklute.backend.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> getByName(String name);
    List<Person> findAllByKeycloakUser(KeycloakUser keycloakUser);
}
