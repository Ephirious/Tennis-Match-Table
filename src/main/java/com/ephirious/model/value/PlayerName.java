package com.ephirious.model.value;


import com.ephirious.exception.domain.InvalidPlayerNameException;
import lombok.*;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerName {
    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 32;

    private final String name;

    public static PlayerName of(@NonNull String name) {
        ensureName(name);
        return new PlayerName(name.trim());
    }

    public String value() {
        return name;
    }

    private static void ensureName(String name) {
        ensureNotBlank(name);
        ensureLength(name);
        ensureOneWhitespaceInMiddle(name);
        ensureWithoutDigits(name);
        ensureLatinCharacters(name);
    }

    private static void ensureNotBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidPlayerNameException(
                    "The name must not be null or empty",
                    "The variable 'name' is empty string"
            );
        }
    }

    private static void ensureWithoutDigits(String name) {
        if (name.codePoints().anyMatch(Character::isDigit)) {
            throw new InvalidPlayerNameException(
                    "The name must not contain digits",
                    "The name '%s' contains digits".formatted(name)
            );
        }
    }

    private static void ensureOneWhitespaceInMiddle(String name) {
        if (name.trim().codePoints().filter(Character::isWhitespace).count() > 1) {
            throw new InvalidPlayerNameException(
                    "The name can contain only one a whitespace in the middle",
                    "The name '%s' contains a lot of whitespaces in the middle".formatted(name)
            );
        }
    }

    private static void ensureLatinCharacters(String name) {
        if (name.trim().codePoints().anyMatch((ch) -> isNotLatin(ch) && !Character.isWhitespace(ch))) {
            throw new InvalidPlayerNameException(
                    "The name must contain only Latin characters",
                    "The name '%s' contains not Latin characters"
            );
        }
    }

    private static void ensureLength(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new InvalidPlayerNameException(
                    "The name must contain between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH),
                    "The name '%s' contains %d characters, but expected between %d and %d characters"
                            .formatted(name, name.length(), MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

    private static boolean isNotLatin(int ch) {
        return !((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }
}
