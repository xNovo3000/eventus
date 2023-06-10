package io.github.xnovo3000.eventus.api.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveSubscriptionDto {

    @NotNull
    private String username;

}