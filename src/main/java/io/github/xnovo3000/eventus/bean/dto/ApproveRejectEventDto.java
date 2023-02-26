package io.github.xnovo3000.eventus.bean.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApproveRejectEventDto {

    @NotNull
    private Long eventId;

}