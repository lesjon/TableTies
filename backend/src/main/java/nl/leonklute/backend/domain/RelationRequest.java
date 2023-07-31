package nl.leonklute.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class RelationRequest {

    private Integer person1;

    private Integer person2;

    private Double relationStrength;

    private Integer eventId;
}