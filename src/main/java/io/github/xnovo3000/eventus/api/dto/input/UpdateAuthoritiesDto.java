package io.github.xnovo3000.eventus.api.dto.input;

import lombok.Data;

@Data
public class UpdateAuthoritiesDto {
    private String eventManager;
    private String userManager;
}