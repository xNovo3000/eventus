package io.github.xnovo3000.eventus.implementation.util;

import io.github.xnovo3000.eventus.bean.entity.Authority;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Component
@Profile("default")
@AllArgsConstructor
public class IApplicationRunnerDefault implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(IApplicationRunnerDefault.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("IApplicationRunnerDefault: running");
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
        // Create more users to test with
        LOGGER.info("Creating 100 test user");
        IntStream.range(0, 100)
                .mapToObj(id -> {
                    val newUser = new User();
                    newUser.setUsername("user" + id);
                    newUser.setPassword(passwordEncoder.encode("user" + id));
                    newUser.setEmail("user" + id + "@eventus.com");
                    newUser.setActive(true);
                    return newUser;
                })
                .forEach(user -> {
                    if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
                        try {
                            userRepository.save(user);
                        } catch (Exception e) {
                            LOGGER.error("Failed to save user", e);
                        }
                    }
                });
    }

}