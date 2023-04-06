package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.util.AuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Configuration for anything related to entity auditing
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
public class EntityAuditingConfiguration {

    /**
     * Create the user auditor
     *
     * @param authenticationFacade The authentication facade
     * @return A lambda returning the current logged user if present
     */
    @Bean
    public AuditorAware<User> auditorAware(AuthenticationFacade authenticationFacade) {
        return () -> authenticationFacade.getUserDetails().map(JpaUserDetails::getUser);
    }

    /**
     * Create the date auditor
     *
     * @return A lambda returning the current OffsetDateTime
     */
    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}