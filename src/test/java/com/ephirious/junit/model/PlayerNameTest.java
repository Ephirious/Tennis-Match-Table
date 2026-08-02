package com.ephirious.junit.model;

import com.ephirious.exception.domain.InvalidPlayerNameException;
import com.ephirious.model.value.player.PlayerName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerNameTest {
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowWhenNameNullOrEmpty(String name) {
        assertThatThrownBy(() -> PlayerName.of(name)).isInstanceOfAny(
                NullPointerException.class,
                InvalidPlayerNameException.class
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "T e s t"
    })
    void shouldForbidWhitespacesInMiddle(String name) {
        assertThrows(InvalidPlayerNameException.class, () -> PlayerName.of(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " Test",
            "Test "
    })
    void shouldAllowWhitespacesAtBorders(String name) {
        assertDoesNotThrow(() -> PlayerName.of(name));
    }

    @ParameterizedTest
    @MethodSource("validNameLength")
    void shouldValidateNameByMaxLength(String name) {
        assertDoesNotThrow(() -> PlayerName.of(name));
    }

    @ParameterizedTest
    @MethodSource("invalidNameLength")
    void shouldForbidInvalidNameLength(String name) {
        assertThrows(InvalidPlayerNameException.class, () -> PlayerName.of(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1Test",
            "T2est",
            "Test3"
    })
    void shouldForbidDigitsInName(String name) {
        assertThrows(InvalidPlayerNameException.class, () -> PlayerName.of(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ",Test",
            "!Test",
            " TeSttt."
    })
    void shouldForbidNoLatinCharacters(String name) {
        assertThrows(InvalidPlayerNameException.class, () -> PlayerName.of(name));
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
