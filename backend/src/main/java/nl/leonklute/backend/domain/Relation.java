package nl.leonklute.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "person1_id")
    private Person person1;

    @ManyToOne
    @JoinColumn(name = "person2_id")
    private Person person2;

    @Column(nullable = false)
    private Double relationStrength;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}