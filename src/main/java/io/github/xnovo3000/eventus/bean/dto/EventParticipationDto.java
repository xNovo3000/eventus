package io.github.xnovo3000.eventus.bean.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventParticipationDto {
    private String username;
    private OffsetDateTime creationDate;
}