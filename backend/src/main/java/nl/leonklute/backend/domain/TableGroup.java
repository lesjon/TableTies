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
    @JoinColumn(name = "event_id")
    private Event event;
}