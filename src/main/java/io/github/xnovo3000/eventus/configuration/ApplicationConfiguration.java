package io.github.xnovo3000.eventus.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
@EntityScan(basePackages = "io.github.xnovo3000.eventus.bean.entity")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.mvc.repository")
public class ApplicationConfiguration {

    @Bean
    public Random random() {
        return new SecureRandom();
    }

}