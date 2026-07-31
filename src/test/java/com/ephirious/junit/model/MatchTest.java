package com.ephirious.junit.model;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.aggregate.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import xyz.block.uuidv7.UUIDv7;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatchTest {
    private UUID first;
    private UUID second;
    private Match match;

    @BeforeEach
    public void initMatch() {
        first = UUIDv7.generate();
        second = UUIDv7.generate();
        match = new Match(first, second);
    }

    @Test
    void shouldThrowWhenTryToGetWinnerButItNotDefine() {
        assertThrows(ContractViolationException.class, () -> match.winner());
    }

    @Test
    void shouldGetFirstWinner() {
        int ballsToWin = 48;
        for (int i = 0; i < ballsToWin ; i++) {
            match.pointTo(first);
        }
        assertThat(match.winner()).isEqualTo(first);
    }

    @Test
    void shouldGetSecondWinner() {
        int ballsToWin = 48;
        for (int i = 0; i < ballsToWin ; i++) {
            match.pointTo(second);
        }
        assertThat(match.winner()).isEqualTo(second);
    }

    @Test
    void shouldThroeWhenCreateMatchWithSamePlayerId() {
        UUID first = UUID.randomUUID();
        assertThatThrownBy(() -> new Match(first, first))
                .isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUuid")
    void shouldThrowWhenCreateMatchWithNullPlayerId(UUID first, UUID second) {
        assertThatThrownBy(() -> new Match(first, second))
                .isInstanceOfAny(ContractViolationException.class, NullPointerException.class);
    }

    public static Stream<Arguments> provideInvalidUuid(){
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, UUID.randomUUID()),
                Arguments.of(UUID.randomUUID(), null)
        );
    }
}
