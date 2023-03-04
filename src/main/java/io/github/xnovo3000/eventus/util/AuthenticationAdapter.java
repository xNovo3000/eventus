package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.security.JpaUserDetails;

import java.util.Optional;

public interface AuthenticationAdapter {
    Optional<JpaUserDetails> getUserDetails();
}