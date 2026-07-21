package com.ephirious.junit.model;

import com.ephirious.model.value.PlayerName;
import com.ephirious.util.validator.AbstractValidator;
import com.ephirious.util.validator.NameValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.IntPredicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerNameTest {
    private static AbstractValidator<IntPredicate, String> validator;

    @BeforeAll
    static void initValidator() {
        validator = new NameValidator();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowWhenNameNullOrEmpty(String name) {
        assertThrows(IllegalArgumentException.class, () -> PlayerName.of(name, validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Te st",
            "T e s t"
    })
    void shouldForbidWhitespacesInMiddle(String name) {
        assertThrows(IllegalArgumentException.class, () -> PlayerName.of(name, validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " Test",
            "Test "
    })
    void shouldAllowWhitespacesAtBorders(String name) {
        assertDoesNotThrow(() -> PlayerName.of(name, validator));
    }

    @ParameterizedTest
    @MethodSource("validNameLength")
    void shouldValidateNameByMaxLength(String name) {
        assertDoesNotThrow(() -> PlayerName.of(name, validator));
    }

    @ParameterizedTest
    @MethodSource("invalidNameLength")
    void shouldForbidInvalidNameLength(String name) {
        assertThrows(IllegalArgumentException.class, () -> PlayerName.of(name, validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1Test",
            "T2est",
            "Test3"
    })
    void shouldForbidDigitsInName(String name) {
        assertThrows(IllegalArgumentException.class, () -> PlayerName.of(name, validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ",Test",
            "!Test",
            " TeSttt."
    })
    void shouldForbidNoLatinCharacters(String name) {
        assertThrows(IllegalArgumentException.class, () -> PlayerName.of(name, validator));
    }


    private static Stream<String> validNameLength() {
        return Stream.of(
                "t".repeat(PlayerName.MAX_LENGTH - 1),
                "t".repeat(PlayerName.MAX_LENGTH)
        );
    }

    private static Stream<String> invalidNameLength() {
        return Stream.of(
                "t".repeat(PlayerName.MAX_LENGTH + 1)
        );
    }
}
