package nl.leonklute.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer target;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "keycloak_user_id")
    private KeycloakUser keycloakUser;
}