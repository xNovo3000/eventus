package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.util.FirstBootApplicationRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "io.github.xnovo3000.eventus.entity")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.repository")
public class EventusConfiguration {

    @Bean
    public ApplicationRunner applicationRunner() {
        return new FirstBootApplicationRunner();
    }

}