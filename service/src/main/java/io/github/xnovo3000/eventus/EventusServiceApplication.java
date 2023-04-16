package io.github.xnovo3000.eventus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 */
@SpringBootApplication(scanBasePackages = "io.github.xnovo3000.eventus")
public class EventusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventusServiceApplication.class, args);
    }

}