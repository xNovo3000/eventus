package io.github.xnovo3000.eventus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.xnovo3000.eventus")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.repository")
@EntityScan(basePackages = "io.github.xnovo3000.eventus.entity")
public class EventusApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventusApplication.class, args);
    }

}
