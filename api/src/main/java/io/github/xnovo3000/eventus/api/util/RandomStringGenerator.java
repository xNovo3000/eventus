package io.github.xnovo3000.eventus.api.util;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class used to generate random strings
 */
public interface RandomStringGenerator {

    /**
     * Generates an alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    @NotNull String generateAlphanumericString(int characters);

    /**
     * Generates a lowercase alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    @NotNull String generateLowercaseAlphanumericString(int characters);

    /**
     * Generates a user-safe alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    @NotNull String generateSafeAlphanumericString(int characters);

}