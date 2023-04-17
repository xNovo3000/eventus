package io.github.xnovo3000.eventus.bootstrap;

import io.github.xnovo3000.eventus.api.entity.Authority;
import io.github.xnovo3000.eventus.api.entity.User;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Bootstrap runner that implements the command pattern. This function
 * runs always and generates the admin user if it does not exist
 */
@Component
@AllArgsConstructor
@Slf4j
public class IGenerateAdminUserBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("IGenerateAdminUserBootstrap: running");
        // Check if admin user is present
        if (userRepository.findByUsername("admin").isEmpty()) {
            log.info("Admin user not present, creating one");
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
                log.info("Successfully created admin user");
            } catch (Exception e) {
                log.error("Cannot create admin user", e);
                throw e;
            }
        }
    }

}