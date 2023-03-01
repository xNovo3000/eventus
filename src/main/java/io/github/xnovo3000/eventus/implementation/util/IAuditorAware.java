package io.github.xnovo3000.eventus.implementation.util;

import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.util.AuthenticationAdapter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@AllArgsConstructor
public class IAuditorAware implements AuditorAware<User> {

    private final AuthenticationAdapter authenticationAdapter;

    @Override
    public Optional<User> getCurrentAuditor() {
        return authenticationAdapter.getAuthenticatedUser();
    }

}