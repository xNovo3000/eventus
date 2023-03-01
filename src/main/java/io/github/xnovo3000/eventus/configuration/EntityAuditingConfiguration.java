package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.implementation.util.IAuditorAware;
import io.github.xnovo3000.eventus.util.AuthenticationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
public class EntityAuditingConfiguration {

    @Bean
    public AuditorAware<User> auditorAware(AuthenticationAdapter authenticationAdapter) {
        return new IAuditorAware(authenticationAdapter);
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}