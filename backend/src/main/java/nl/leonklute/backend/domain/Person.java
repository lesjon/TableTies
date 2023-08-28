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
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "table_group_id", nullable = true)
    private TableGroup tableGroup;
}
