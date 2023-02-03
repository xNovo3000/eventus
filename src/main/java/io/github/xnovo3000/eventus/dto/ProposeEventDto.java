package io.github.xnovo3000.eventus.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ProposeEventDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @Future
    private LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @Future
    private LocalDateTime end;

}