package nl.leonklute.backend.domain;

import lombok.Data;

@Data
public class PersonRequest {

    private String name;

    private Integer eventId;
}
