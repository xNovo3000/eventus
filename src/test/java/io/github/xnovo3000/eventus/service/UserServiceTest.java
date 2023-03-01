package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.bean.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.mvc.service.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceTest {

    private final UserService userService;

    @Test
    @Order(1)
    public void registerNewUser_Success() {
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user0@eventus.com");
        registerFormDto.setUsername("user0");
        Assertions.assertTrue(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(2)
    public void registerNewUser_FailUsernameAlreadyExist() {
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user1@eventus.com");
        registerFormDto.setUsername("user0");
        Assertions.assertFalse(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(3)
    public void registerNewUser_FailEmailAlreadyExist() {
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user0@eventus.com");
        registerFormDto.setUsername("user1");
        Assertions.assertFalse(userService.registerNewUser(registerFormDto));
    }

}