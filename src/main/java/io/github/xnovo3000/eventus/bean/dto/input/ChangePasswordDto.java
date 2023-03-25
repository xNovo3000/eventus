package io.github.xnovo3000.eventus.bean.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordDto {

    @NotBlank
    @Length(min = 8)
    private String newPassword;

}