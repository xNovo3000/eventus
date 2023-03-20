package io.github.xnovo3000.eventus.bean.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAuthoritiesDto {
    private String eventManager;
    private String userManager;
}