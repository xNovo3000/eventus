package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.api.dto.input.ChangePasswordDto;
import io.github.xnovo3000.eventus.api.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.api.dto.output.UserDto;
import io.github.xnovo3000.eventus.api.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * UserService proxy to test UserService implementation when in 'default' profile
 */
@Service
@Profile("default")
@Primary
@Transactional
@RequiredArgsConstructor
@Slf4j
public class IUserServiceDefaultProxy implements UserService {

    private final IUserService userService;

    @Override
    public boolean registerNewUser(@NotNull RegisterFormDto registerFormDto) {
        log.info("registerNewUser called with payload: " + registerFormDto);
        // Crash if email is 'crash_test@eventus.com'
        if (Objects.equals(registerFormDto.getEmail(), "crash_test@eventus.com")) {
            throw new RuntimeException("Eventus demo exception to test error page");
        }
        // Run proxied function
        return userService.registerNewUser(registerFormDto);
    }

    @Override
    public @NotNull Page<UserDto> getByFilter(int pageNumber, @Nullable String username) {
        // Run proxied function
        return userService.getByFilter(pageNumber, username);
    }

    @Override
    public boolean disable(long userId) {
        log.info("disable called with userId: " + userId);
        // Run proxied function
        return userService.disable(userId);
    }

    @Override
    public boolean enable(long userId) {
        log.info("enable called with userId: " + userId);
        // Run proxied function
        return userService.enable(userId);
    }

    @Override
    public boolean resetPassword(long userId) {
        log.info("resetPassword called with userId: " + userId);
        // Run proxied function
        return userService.resetPassword(userId);
    }

    @Override
    public boolean updateAuthorities(long userId, @NotNull List<String> authorities) {
        log.info("updatePermissions called with userId: " + userId + " and authorities: " + authorities);
        // Run proxied function
        return userService.updateAuthorities(userId, authorities);
    }

    @Override
    public boolean changePassword(@NotNull ChangePasswordDto dto) {
        log.info("changePassword called with payload: " + dto);
        // Run proxied function
        return userService.changePassword(dto);
    }

}