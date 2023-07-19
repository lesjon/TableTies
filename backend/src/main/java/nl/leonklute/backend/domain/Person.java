package nl.leonklute.backend.domain;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "keycloak_user_id")
    private KeycloakUser keycloakUser;
}
