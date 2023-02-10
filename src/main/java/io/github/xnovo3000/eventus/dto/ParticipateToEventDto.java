package io.github.xnovo3000.eventus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipateToEventDto {

    @NotNull
    private Long eventId;

}