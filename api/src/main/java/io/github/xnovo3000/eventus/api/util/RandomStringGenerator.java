package io.github.xnovo3000.eventus.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

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
    @NotNull String generateAlphanumericString(@Range(from = 0, to = Integer.MAX_VALUE) int characters);

    /**
     * Generates a lowercase alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    @NotNull String generateLowercaseAlphanumericString(@Range(from = 0, to = Integer.MAX_VALUE) int characters);

    /**
     * Generates a user-safe alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    @NotNull String generateSafeAlphanumericString(@Range(from = 0, to = Integer.MAX_VALUE) int characters);

}