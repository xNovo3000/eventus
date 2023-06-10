package io.github.xnovo3000.eventus.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Configuration class for anything related to the application
 */
@Configuration
@EntityScan(basePackages = "io.github.xnovo3000.eventus.api.entity")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.api.repository")
public class ApplicationConfiguration {

    /**
     * Defines an implementation of Random, returning a SecureRandom
     *
     * @return the Random interface
     */
    @Bean
    public Random random() {
        return new SecureRandom();
    }

}