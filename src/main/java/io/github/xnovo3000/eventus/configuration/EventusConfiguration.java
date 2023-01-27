package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.util.FirstBootApplicationRunner;
import io.github.xnovo3000.eventus.util.RandomStringGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
@EntityScan(basePackages = "io.github.xnovo3000.eventus.entity")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.repository")
public class EventusConfiguration {

    @Bean
    public Random random() {
        return new SecureRandom();
    }

    @Bean
    public RandomStringGenerator randomStringGenerator() {
        return new RandomStringGenerator(random());
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new FirstBootApplicationRunner(userRepository, passwordEncoder, randomStringGenerator());
    }

}