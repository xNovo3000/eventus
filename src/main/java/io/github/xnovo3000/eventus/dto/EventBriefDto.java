package io.github.xnovo3000.eventus.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventBriefDto {
    private Long id;
    private String name;
    private String description;
    private String creatorUsername;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private Integer seats;
    private Integer occupiedSeats;
}