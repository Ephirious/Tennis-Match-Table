package com.ephirious.model.value;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerName {
    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 32;

    private final String name;

    public static PlayerName of(String name) {
        ensureName(name);
        return new PlayerName(name);
    }

    public String value() {
        return name;
    }

    private static void ensureName(String name) {
        ensureNotBlank(name);
        ensureWithoutDigits(name);
        ensureLatinCharacters(name);
    }

    private static void ensureNotBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The name must not be null or empty");
        }
    }

    private static void ensureWithoutDigits(String name) {
        if (name.codePoints().anyMatch(Character::isDigit)) {
            throw new IllegalStateException("The name must not contain digits");
        }
    }

    private static void ensureLatinCharacters(String name) {
        if (name.codePoints().anyMatch((ch) -> isNotLatin(ch) || Character.isWhitespace(ch))) {
            throw new IllegalArgumentException("The name must contain only Latin characters");
        }
    }

    private static boolean isNotLatin(int ch) {
        return !((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }
}
