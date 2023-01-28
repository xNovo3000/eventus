package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.entity.Authority;
import io.github.xnovo3000.eventus.entity.User;
import io.github.xnovo3000.eventus.repository.UserRepository;
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

    private final List<String> defaultAuthorities = List.of("USER_MANAGER", "EVENT_MANAGER");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Check if admin user is present
        if (userRepository.findByUsername("admin").isEmpty()) {
            LOGGER.info("Admin user not present, creating one");
            // Create the user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail("admin@eventus");
            admin.setActive(true);
            // Create the authorities
            List<Authority> authorities = defaultAuthorities.stream()
                    .map(string -> {
                        Authority authority = new Authority();
                        authority.setName(string);
                        authority.setUser(admin);
                        return authority;
                    })
                    .toList();
            admin.setAuthorities(authorities);
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