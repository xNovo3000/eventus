package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.bean.entity.Authority;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@AllArgsConstructor
public class FirstBootApplicationRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstBootApplicationRunner.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Check if admin user is present
        if (userRepository.findByUsername("admin").isEmpty()) {
            LOGGER.info("Admin user not present, creating one");
            // Create the user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@eventus");
            admin.setActive(true);
            // Create the user manager authority
            Authority userManagerAuthority = new Authority();
            userManagerAuthority.setName("USER_MANAGER");
            userManagerAuthority.setUser(admin);
            // Give it to the admin
            admin.setAuthorities(List.of(userManagerAuthority));
            // Save the user
            try {
                userRepository.save(admin);
                LOGGER.info("Successfully created admin user");
            } catch (Exception e) {
                LOGGER.error("Cannot create admin user", e);
                throw e;
            }
        }

    }

}