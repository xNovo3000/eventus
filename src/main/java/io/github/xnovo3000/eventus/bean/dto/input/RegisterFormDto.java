package io.github.xnovo3000.eventus.bean.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterFormDto {

    @Email
    @NotNull
    private String email;

    @NotBlank
    private String username;

}