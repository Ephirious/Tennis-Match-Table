package com.ephirious.model.value;


import com.ephirious.util.validator.AbstractValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.IntPredicate;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerName {
    String name;

    public static PlayerName of(String name, AbstractValidator<IntPredicate, String> nameValidator) {
        nameValidator.ensure(name);
        return new PlayerName(name);
    }
}
