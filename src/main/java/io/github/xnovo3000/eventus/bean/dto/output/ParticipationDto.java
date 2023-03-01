package io.github.xnovo3000.eventus.bean.dto.output;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ParticipationDto {
    private String username;
    private OffsetDateTime creationDate;
}