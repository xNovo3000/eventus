package io.github.xnovo3000.eventus.api.dto.output;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventCardDto {
    private Long id;
    private String name;
    private String description;
    private String creatorUsername;
    private OffsetDateTime creationDate;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private Boolean approved;
    private Integer seats;
    private Integer occupiedSeats;
    private Boolean canSubscribe;
    private Boolean canUnsubscribe;
}