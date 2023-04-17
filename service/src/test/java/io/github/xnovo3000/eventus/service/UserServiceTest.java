package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.annotation.EventusTest;
import io.github.xnovo3000.eventus.api.dto.input.ChangePasswordDto;
import io.github.xnovo3000.eventus.api.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EventusTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceTest {

    private final UserService userService;
    private final UserRepository userRepository;

    private Long adminUserId;

    @BeforeAll
    @Transactional
    public void setup() {
        // Get admin user ID
        adminUserId = userRepository.findByUsername("admin").orElseThrow().getId();
        // Disable user20 for testing
        val user20 = userRepository.findByUsername("user20").orElseThrow();
        user20.setActive(false);
        userRepository.save(user20);
    }

    @Test
    @Order(1)
    public void registerNewUser_UsernameAlreadyTaken() {
        val registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user101@eventus.com");
        registerFormDto.setUsername("user1");
        Assertions.assertFalse(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(1)
    public void registerNewUser_EmailAlreadyTaken() {
        val registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user1@eventus.com");
        registerFormDto.setUsername("user101");
        Assertions.assertFalse(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(2)
    public void registerNewUser_Success() {
        val registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("user101@eventus.com");
        registerFormDto.setUsername("user101");
        Assertions.assertTrue(userService.registerNewUser(registerFormDto));
    }

    @Test
    @Order(10)
    public void disable_AdminUserId() {
        Assertions.assertFalse(userService.disable(adminUserId));
    }

    @Test
    @Order(11)
    public void disable_Success() {
        Assertions.assertTrue(userService.disable(21));
    }

    @Test
    @Order(12)
    public void enable_AdminUserInvalid() {
        Assertions.assertFalse(userService.enable(adminUserId));
    }

    @Test
    @Order(13)
    public void enable_Success() {
        Assertions.assertTrue(userService.enable(21));
    }

    @Test
    @Order(14)
    public void resetPassword_AdminInvalid() {
        Assertions.assertFalse(userService.resetPassword(adminUserId));
    }

    @Test
    @Order(14)
    public void resetPassword_Success() {
        Assertions.assertTrue(userService.resetPassword(30));
    }

    @Test
    @Order(15)
    public void updateAuthorities_AdminInvalid() {
        Assertions.assertFalse(userService.updateAuthorities(adminUserId, List.of("EVENT_MANAGER")));
    }

    @Test
    @Order(15)
    public void updateAuthorities_Success() {
        Assertions.assertTrue(userService.updateAuthorities(20, List.of("EVENT_MANAGER")));
    }

    @Test
    @Order(16)
    @WithUserDetails("admin")
    public void changePassword_AdminInvalid() {
        val changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setNewPassword("new_password");
        Assertions.assertTrue(userService.changePassword(changePasswordDto));
    }

    @Test
    @Order(16)
    @WithUserDetails("user10")
    public void changePassword_Success() {
        val changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setNewPassword("new_password");
        Assertions.assertTrue(userService.changePassword(changePasswordDto));
    }

}