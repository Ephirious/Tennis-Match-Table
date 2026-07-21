package com.ephirious.util.validator;

import com.ephirious.model.value.PlayerName;

import java.util.List;
import java.util.function.IntPredicate;

public final class NameValidator extends AbstractValidator<IntPredicate, String> {
    private static final List<ValidationRule<IntPredicate>> RULES = List.of(
            new ValidationRule<>(Character::isWhitespace, "The name must not contain whitespaces"),
            new ValidationRule<>(Character::isDigit, "The name must not contain digits"),
            new ValidationRule<>(NameValidator::isNotLatin, "The name must contain only Latin characters")
    );

    public NameValidator() {
        super(RULES);
    }

    public void ensure(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The name must not be null or empty");
        }

        if (name.length() > PlayerName.MAX_LENGTH) {
            throw new IllegalArgumentException("The name must contain no more than 64 characters");
        }

        RULES.stream()
                .filter(rule -> name.trim().codePoints().anyMatch(rule.predicate()))
                .findFirst()
                .ifPresent(rule -> {
                    throw new IllegalArgumentException(rule.errorMessage());
                });
    }

    private static boolean isNotLatin(int ch) {
        return !((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }
}
