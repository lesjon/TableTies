package nl.leonklute.backend.repository;

import nl.leonklute.backend.domain.KeycloakUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeycloakUserRepository extends JpaRepository<KeycloakUser, Integer> {
    Optional<KeycloakUser> findByUsername(String username);
}
