package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.RegisterFormDto;

public interface UserService {

    /**
     * Register a new user
     *
     * @param registerFormDto The payload
     * @return True if the user has been created, false otherwise
     */
    boolean registerNewUser(RegisterFormDto registerFormDto);

}