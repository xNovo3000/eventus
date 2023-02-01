package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.RegisterFormDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private final UserService userService;

    public UserServiceTest(@Autowired UserService userService) {
        this.userService = userService;
    }

    @Test
    @Order(1)
    public void createUser_Success() {
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user0@eventus.com");
        registerFormDto.setUsername("user0");
        Assertions.assertTrue(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(2)
    public void createUser_FailUsernameAlreadyExist() {
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user1@eventus.com");
        registerFormDto.setUsername("user0");
        Assertions.assertFalse(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(3)
    public void createUser_FailEmailAlreadyExist() {
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user0@eventus.com");
        registerFormDto.setUsername("user1");
        Assertions.assertFalse(userService.registerNewUser(registerFormDto));
    }

}