package nl.leonklute.backend.domain;

import lombok.Data;

@Data
public class TableGroupRequest {


    private Integer target;

    private Integer capacity;

    private Integer eventId;
}