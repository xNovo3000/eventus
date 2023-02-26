package io.github.xnovo3000.eventus.mvc.service;

import io.github.xnovo3000.eventus.bean.dto.RegisterFormDto;

public interface UserService {

    /**
     * Register a new user
     *
     * @param registerFormDto The payload
     * @return True if the user has been created, false if email and/or username is already taken
     */
    boolean registerNewUser(RegisterFormDto registerFormDto);

}