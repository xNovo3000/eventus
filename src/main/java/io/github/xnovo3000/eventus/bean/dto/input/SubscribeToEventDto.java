package io.github.xnovo3000.eventus.bean.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscribeToEventDto {

    @NotNull
    private Long eventId;

}