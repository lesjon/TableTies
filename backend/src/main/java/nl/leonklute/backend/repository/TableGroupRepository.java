package nl.leonklute.backend.repository;

import nl.leonklute.backend.domain.TableGroup;
import nl.leonklute.backend.domain.KeycloakUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Integer> {
    List<TableGroup> findAllByKeycloakUser(KeycloakUser keycloakUser);
}
