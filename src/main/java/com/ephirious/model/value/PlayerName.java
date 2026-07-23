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
            throw new IllegalArgumentException("The name must not be null or empty");
        }
    }

    private static void ensureWithoutDigits(String name) {
        if (name.codePoints().anyMatch(Character::isDigit)) {
            throw new IllegalArgumentException("The name must not contain digits");
        }
    }

    private static void ensureOneWhitespaceInMiddle(String name) {
        if (name.trim().codePoints().filter(Character::isWhitespace).count() > 1) {
            throw new IllegalArgumentException("The name can contain only one a whitespace in the middle");
        }
    }

    private static void ensureLatinCharacters(String name) {
        if (name.trim().codePoints().anyMatch((ch) -> isNotLatin(ch) && !Character.isWhitespace(ch))) {
            throw new IllegalArgumentException("The name must contain only Latin characters");
        }
    }

    private static void ensureLength(String name) {
        if (name.length() < PlayerName.MIN_LENGTH || name.length() > PlayerName.MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "The name must contain between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

    private static boolean isNotLatin(int ch) {
        return !((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }
}
