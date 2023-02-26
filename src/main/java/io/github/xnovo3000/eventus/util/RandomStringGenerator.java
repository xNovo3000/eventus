package io.github.xnovo3000.eventus.util;

public interface RandomStringGenerator {

    /**
     * Generates an alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    String generateAlphanumericString(int characters);

    /**
     * Generates a lowercase alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    String generateLowercaseAlphanumericString(int characters);

    /**
     * Generates a user-safe alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    String generateSafeAlphanumericString(int characters);

}