package io.github.xnovo3000.eventus.implementation.util;

import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.util.AuthenticationAdapter;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IAuthenticationAdapter implements AuthenticationAdapter {

    @Override
    public String getUsername() {
        return maybeGetUserDetails().map(UserDetails::getUsername).orElse(null);
    }

    @Override
    public Optional<User> getAuthenticatedUser() {
        val maybeUserDetails = maybeGetUserDetails();
        if (maybeUserDetails.isEmpty()) {
            return Optional.empty();
        }
        val userDetails = maybeUserDetails.get();
        if (!(userDetails instanceof JpaUserDetails)) {
            return Optional.empty();
        }
        return Optional.of(((JpaUserDetails) userDetails).getUser());
    }

    private Optional<UserDetails> maybeGetUserDetails() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        val principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return Optional.empty();
        }
        return Optional.of((UserDetails) principal);
    }

}
