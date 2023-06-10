package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.api.dto.input.ChangePasswordDto;
import io.github.xnovo3000.eventus.api.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.api.dto.output.UserDto;
import io.github.xnovo3000.eventus.api.entity.Authority;
import io.github.xnovo3000.eventus.api.entity.User;
import io.github.xnovo3000.eventus.api.repository.AuthorityRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.api.service.EmailService;
import io.github.xnovo3000.eventus.api.service.UserService;
import io.github.xnovo3000.eventus.api.util.DtoMapper;
import io.github.xnovo3000.eventus.api.util.RandomStringGenerator;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.security.JpaAuthenticationProxy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService default implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class IUserService implements UserService {

    private final DtoMapper dtoMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator randomStringGenerator;
    private final JpaAuthenticationProxy authenticationProxy;
    
    @Value("${io.github.xnovo3000.eventus.user_page_size}") private Integer pageSize;

    @Override
    public boolean registerNewUser(@NotNull RegisterFormDto registerFormDto) {
        log.info("registerNewUser called with payload: " + registerFormDto);
        // Check if username and/or password already exist in the database
        if (userRepository.findByEmail(registerFormDto.getEmail()).isPresent()) {
            log.info("Email " + registerFormDto.getEmail() + " already exist");
            return false;
        }
        if (userRepository.findByUsername(registerFormDto.getUsername()).isPresent()) {
            log.info("Username " + registerFormDto.getUsername() + " already exist");
            return false;
        }
        // Generate a new password
        String password = randomStringGenerator.generateSafeAlphanumericString(8);
        // Generate the new user
        User user = new User();
        user.setEmail(registerFormDto.getEmail());
        user.setUsername(registerFormDto.getUsername());
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        try {
            // Send email with the password
            emailService.sendPasswordViaEmail(user.getEmail(), password);
            // Save user in the database
            userRepository.save(user);
            // Return success
            return true;
        } catch (Throwable e) {
            log.error("Cannot save user or send password via email", e);
            return false;
        }
    }

    @Override
    public @NotNull Page<UserDto> getByFilter(int pageNumber, @Nullable String username) {
        // Create the page request
        val pageable = PageRequest.of(pageNumber - 1, pageSize);
        // Return if username is null or not
        if (username != null) {
            return userRepository.findAllByUsernameContainingIgnoreCaseOrderByUsernameAsc(username, pageable)
                    .map(dtoMapper::toUserDto);
        } else {
            return userRepository.findAllByOrderByUsernameAsc(pageable)
                    .map(dtoMapper::toUserDto);
        }
    }

    @Override
    public boolean disable(long userId) {
        log.info("disable called with userId: " + userId);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            log.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (user.getUsername().equals("admin")) {
            log.info("Admin user cannot be disabled");
            return false;
        }
        // Disable the user and try to save
        user.setActive(false);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Cannot update user", e);
            return false;
        }
    }

    @Override
    public boolean enable(long userId) {
        log.info("enable called with userId: " + userId);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            log.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (user.getUsername().equals("admin")) {
            log.info("Admin user cannot be enabled");
            return false;
        }
        // Enable the user and try to save
        user.setActive(true);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Cannot update user", e);
            return false;
        }
    }

    @Override
    public boolean resetPassword(long userId) {
        log.info("resetPassword called with userId: " + userId);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            log.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (user.getUsername().equals("admin")) {
            log.info("Admin user's password cannot be reset");
            return false;
        }
        // Get a new password, send it via email and save
        val newPassword = randomStringGenerator.generateSafeAlphanumericString(8);
        emailService.sendPasswordViaEmail(user.getEmail(), newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Cannot update user", e);
            return false;
        }
    }

    @Override
    public boolean updateAuthorities(long userId, @NotNull List<String> authorities) {
        log.info("updatePermissions called with userId: " + userId + " and authorities: " + authorities);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            log.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (user.getUsername().equals("admin")) {
            log.info("Admin user's authorities cannot be changed");
            return false;
        }
        // Remove all authorities for the specific user
        try {
            authorityRepository.deleteAllByUser(user);
        } catch (Exception e) {
            log.error("Cannot delete authorities", e);
            return false;
        }
        // Create new authorities
        val newAuthorities = authorities.stream()
                .map(authorityName -> {
                    val newAuthority = new Authority();
                    newAuthority.setName(authorityName);
                    newAuthority.setUser(user);
                    return newAuthority;
                })
                .toList();
        // Update authorities
        try {
            authorityRepository.saveAll(newAuthorities);
            return true;
        } catch (Exception e) {
            log.error("Cannot create new authorities", e);
            return false;
        }
    }

    @Override
    public boolean changePassword(@NotNull ChangePasswordDto dto) {
        log.info("changePassword called with payload: " + dto);
        // Get current username
        val username = authenticationProxy.getUserDetails()
                .map(JpaUserDetails::getUsername).orElse(null);
        // Get current user
        val maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            log.error("Logged user not found: " + username);
            return false;
        }
        val user = maybeUser.get();
        // Update the password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        // Try to save
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Cannot save user", e);
            return false;
        }
        // Logout the user and return success
        authenticationProxy.logout();
        return true;
    }

}