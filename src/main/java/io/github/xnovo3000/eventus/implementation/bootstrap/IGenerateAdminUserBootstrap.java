package io.github.xnovo3000.eventus.implementation.bootstrap;

import io.github.xnovo3000.eventus.bean.entity.Authority;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class IGenerateAdminUserBootstrap implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(IGenerateAdminUserBootstrap.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        LOGGER.info("IGenerateAdminUserBootstrap: running");
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