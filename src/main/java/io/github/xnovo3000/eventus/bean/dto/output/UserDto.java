package io.github.xnovo3000.eventus.bean.dto.output;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Boolean active;
    private List<String> authorities;
}