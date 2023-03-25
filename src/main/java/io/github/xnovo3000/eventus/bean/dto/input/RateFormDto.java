package io.github.xnovo3000.eventus.bean.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RateFormDto {

    @NotNull
    @Min(1) @Max(5)
    private Integer stars;

    @NotBlank
    @Length(max = 2048)
    private String comment;

}