package io.github.xnovo3000.eventus.util;

import java.util.Random;

public class RandomStringGenerator {

    private final String RND_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWERCASE_RND_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final String SAFE_RND_ALPHABET = "123456789abcdefghimnopqrstuvzABCDEFGHLMNPQRSTUVZ";
    private final Random random;

    public RandomStringGenerator(Random random) {
        this.random = random;
    }

    /**
     * Generates an alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    public String generateAlphanumericString(int characters) {
        StringBuilder stringBuilder = new StringBuilder(characters);
        for (int i = 0; i < characters; i++) {
            stringBuilder.append(RND_ALPHABET.charAt(random.nextInt(RND_ALPHABET.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * Generates a lowercase alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    public String generateLowercaseAlphanumericString(int characters) {
        StringBuilder stringBuilder = new StringBuilder(characters);
        for (int i = 0; i < characters; i++) {
            stringBuilder.append(LOWERCASE_RND_ALPHABET.charAt(random.nextInt(LOWERCASE_RND_ALPHABET.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * Generates a user-safe alphanumeric string
     *
     * @param characters The number of characters to generate
     * @return The resulting string
     */
    public String generateSafeAlphanumericString(int characters) {
        StringBuilder stringBuilder = new StringBuilder(characters);
        for (int i = 0; i < characters; i++) {
            stringBuilder.append(SAFE_RND_ALPHABET.charAt(random.nextInt(SAFE_RND_ALPHABET.length())));
        }
        return stringBuilder.toString();
    }

}