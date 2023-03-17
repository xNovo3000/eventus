package io.github.xnovo3000.eventus.implementation.service;

import io.github.xnovo3000.eventus.bean.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.bean.dto.output.UserDto;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import io.github.xnovo3000.eventus.mvc.service.EmailService;
import io.github.xnovo3000.eventus.mvc.service.UserService;
import io.github.xnovo3000.eventus.util.DtoMapper;
import io.github.xnovo3000.eventus.util.RandomStringGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class IUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserService.class);

    private final int PAGE_SIZE = 24;

    private final DtoMapper dtoMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator randomStringGenerator;

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

}