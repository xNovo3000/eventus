package io.github.xnovo3000.eventus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterFormDto {

    @Email
    private String email;

    @NotBlank
    private String username;

}