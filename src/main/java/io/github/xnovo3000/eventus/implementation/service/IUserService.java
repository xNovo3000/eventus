package io.github.xnovo3000.eventus.implementation.service;

import io.github.xnovo3000.eventus.bean.dto.input.ChangePasswordDto;
import io.github.xnovo3000.eventus.bean.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.bean.dto.output.UserDto;
import io.github.xnovo3000.eventus.bean.entity.Authority;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.bean.validation.BeanValidator;
import io.github.xnovo3000.eventus.mvc.repository.AuthorityRepository;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import io.github.xnovo3000.eventus.mvc.service.EmailService;
import io.github.xnovo3000.eventus.mvc.service.UserService;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.util.AuthenticationAdapter;
import io.github.xnovo3000.eventus.util.DtoMapper;
import io.github.xnovo3000.eventus.util.RandomStringGenerator;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserService.class);

    private final int PAGE_SIZE = 24;

    private final DtoMapper dtoMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator randomStringGenerator;
    private final AuthenticationAdapter authenticationAdapter;

    @Resource private BeanValidator<User> userServiceValidator;

    @Override
    public boolean registerNewUser(RegisterFormDto registerFormDto) {
        LOGGER.info("registerNewUser called with payload: " + registerFormDto);
        // Check if username and/or password already exist in the database
        if (userRepository.findByEmail(registerFormDto.getEmail()).isPresent()) {
            LOGGER.info("Email " + registerFormDto.getEmail() + " already exist");
            return false;
        }
        if (userRepository.findByUsername(registerFormDto.getUsername()).isPresent()) {
            LOGGER.info("Username " + registerFormDto.getUsername() + " already exist");
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
            // Save user in the database
            user = userRepository.save(user);
            // Send email with the password
            emailService.sendPasswordViaEmail(user.getEmail(), password);
            // Return success
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot save user or send password via email", e);
            return false;
        }
    }

    @Override
    public Page<UserDto> getByFilter(int pageNumber, String username) {
        // Create the page request
        val pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
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
    public boolean disable(Long userId) {
        LOGGER.info("disable called with userId: " + userId);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            LOGGER.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (!userServiceValidator.validate(user)) {
            LOGGER.info("Cannot validate user");
            return false;
        }
        // Disable the user and try to save
        user.setActive(false);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot update user", e);
            return false;
        }
    }

    @Override
    public boolean enable(Long userId) {
        LOGGER.info("enable called with userId: " + userId);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            LOGGER.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (!userServiceValidator.validate(user)) {
            LOGGER.info("Cannot validate user");
            return false;
        }
        // Enable the user and try to save
        user.setActive(true);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot update user", e);
            return false;
        }
    }

    @Override
    public boolean resetPassword(Long userId) {
        LOGGER.info("resetPassword called with userId: " + userId);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            LOGGER.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (!userServiceValidator.validate(user)) {
            LOGGER.info("Cannot validate user");
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
            LOGGER.error("Cannot update user", e);
            return false;
        }
    }

    @Override
    public boolean updateAuthorities(Long userId, List<String> authorities) {
        LOGGER.info("updatePermissions called with userId: " + userId + " and authorities: " + authorities);
        // Check if user exists
        val maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            LOGGER.info("User does not exist");
            return false;
        }
        val user = maybeUser.get();
        // Validate
        if (!userServiceValidator.validate(user)) {
            LOGGER.info("Cannot validate user");
            return false;
        }
        // Remove all authorities for the specific user
        try {
            authorityRepository.deleteAllByUser(user);
        } catch (Exception e) {
            LOGGER.error("Cannot delete authorities", e);
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
            LOGGER.error("Cannot create new authorities", e);
            return false;
        }
    }

    @Override
    public boolean changePassword(ChangePasswordDto dto) {
        LOGGER.info("changePassword called with payload: " + dto);
        // Get current username
        val username = authenticationAdapter.getUserDetails()
                .map(JpaUserDetails::getUsername).orElse(null);
        // Get current user
        val maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            LOGGER.error("Logged user not found: " + username);
            return false;
        }
        val user = maybeUser.get();
        // Update the password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        // Try to save
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot save user", e);
            return false;
        }
    }

}