package io.github.xnovo3000.eventus.api.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterFormDto {

    @Email
    @NotNull
    @Length(max = 256)
    private String email;

    @NotBlank
    @Length(max = 256)
    private String username;

}