package io.github.xnovo3000.eventus.serviceimpl;

import io.github.xnovo3000.eventus.dto.RegisterFormDto;
import io.github.xnovo3000.eventus.entity.User;
import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.service.EmailService;
import io.github.xnovo3000.eventus.service.UserService;
import io.github.xnovo3000.eventus.util.RandomStringGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class IUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserService.class);

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator randomStringGenerator;

    @Override
    public boolean registerNewUser(RegisterFormDto registerFormDto) {
        LOGGER.debug("registerNewUser called with payload: " + registerFormDto);
        // Check if username and/or password already exist in the database
        if (userRepository.findByEmail(registerFormDto.getEmail()).isPresent()) {
            LOGGER.debug("Email " + registerFormDto.getEmail() + " already exist");
            return false;
        }
        if (userRepository.findByUsername(registerFormDto.getUsername()).isPresent()) {
            LOGGER.debug("Username " + registerFormDto.getUsername() + " already exist");
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
        // Save user in the database
        user = userRepository.save(user);
        // Send email with the password
        emailService.sendPasswordViaEmail(user.getEmail(), password);
        // Return success
        return true;
    }

}