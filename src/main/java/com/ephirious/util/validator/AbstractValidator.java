package com.ephirious.util.validator;

import java.util.List;

public abstract class AbstractValidator<T, K> {
    public record ValidationRule<T>(T predicate, String errorMessage) {}

    protected final List<ValidationRule<T>> rules;

    protected AbstractValidator(List<ValidationRule<T>> rules) {
        this.rules = rules;
    }

    public abstract void ensure(K object);
}
