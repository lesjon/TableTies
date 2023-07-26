package nl.leonklute.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class RelationRequest {

    private String person1;

    private String person2;

    private Double relationStrength;

    private Integer eventId;
}