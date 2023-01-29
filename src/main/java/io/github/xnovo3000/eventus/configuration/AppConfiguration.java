package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.entity.Event;
import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.util.FirstBootApplicationRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EntityScan(basePackages = "io.github.xnovo3000.eventus.entity")
@EnableJpaRepositories(basePackages = "io.github.xnovo3000.eventus.repository")
public class AppConfiguration {

    @Bean
    public ApplicationRunner applicationRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${io.github.xnovo3000.eventus.admin-password}") String adminPassword
    ) {
        return new FirstBootApplicationRunner(userRepository, passwordEncoder, adminPassword);
    }

    @Bean
    public ModelMapper modelMapper() {
        // Setup mapper
        ModelMapper modelMapper = new ModelMapper();
        // Create custom mappings (not recognized by default)
        modelMapper.typeMap(Event.class, EventBriefDto.class)
                .addMapping(event -> event.getCreator().getUsername(), EventBriefDto::setCreatorUsername)
                .addMapping(event -> event.getHoldings() != null ? event.getHoldings().size() : 0, EventBriefDto::setOccupiedSeats);
        // Return mapper
        return modelMapper;
    }

}