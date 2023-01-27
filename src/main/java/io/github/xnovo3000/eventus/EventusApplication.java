package io.github.xnovo3000.eventus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.xnovo3000.eventus")
public class EventusApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventusApplication.class, args);
    }

}
