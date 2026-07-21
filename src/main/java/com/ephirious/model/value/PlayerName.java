package com.ephirious.model.value;


import com.ephirious.util.validator.AbstractValidator;
import lombok.*;

import java.util.function.IntPredicate;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerName {
    public static final int MAX_LENGTH = 32;

    private final String name;

    public static PlayerName of(String name, AbstractValidator<IntPredicate, String> nameValidator) {
        nameValidator.ensure(name);
        return new PlayerName(name);
    }

    public String value() {
        return name;
    }
}
