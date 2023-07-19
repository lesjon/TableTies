package nl.leonklute.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class KeycloakUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;
}
