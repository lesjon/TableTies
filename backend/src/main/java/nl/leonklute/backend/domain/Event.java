package nl.leonklute.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "keycloak_user_id")
    private KeycloakUser keycloakUser;
}
