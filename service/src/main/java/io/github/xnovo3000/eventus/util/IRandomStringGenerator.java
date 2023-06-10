package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.api.util.RandomStringGenerator;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Default implementation of RandomStringGenerator
 */
@Component
@AllArgsConstructor
public class IRandomStringGenerator implements RandomStringGenerator {

    private static final String RND_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_RND_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String SAFE_RND_ALPHABET = "123456789abcdefghimnopqrstuvzABCDEFGHLMNPQRSTUVZ";

    private final Random random;

    @Override
    public @NotNull String generateAlphanumericString(int characters) {
        StringBuilder stringBuilder = new StringBuilder(characters);
        for (int i = 0; i < characters; i++) {
            stringBuilder.append(RND_ALPHABET.charAt(random.nextInt(RND_ALPHABET.length())));
        }
        return stringBuilder.toString();
    }

    @Override
    public @NotNull String generateLowercaseAlphanumericString(int characters) {
        StringBuilder stringBuilder = new StringBuilder(characters);
        for (int i = 0; i < characters; i++) {
            stringBuilder.append(LOWERCASE_RND_ALPHABET.charAt(random.nextInt(LOWERCASE_RND_ALPHABET.length())));
        }
        return stringBuilder.toString();
    }

    @Override
    public @NotNull String generateSafeAlphanumericString(int characters) {
        StringBuilder stringBuilder = new StringBuilder(characters);
        for (int i = 0; i < characters; i++) {
            stringBuilder.append(SAFE_RND_ALPHABET.charAt(random.nextInt(SAFE_RND_ALPHABET.length())));
        }
        return stringBuilder.toString();
    }

}