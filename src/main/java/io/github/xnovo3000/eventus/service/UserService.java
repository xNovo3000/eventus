package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.RegisterFormDto;

public interface UserService {
    boolean registerNewUser(RegisterFormDto registerFormDto);
}