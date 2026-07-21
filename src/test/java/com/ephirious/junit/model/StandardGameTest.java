package com.ephirious.junit.model;

import com.ephirious.model.value.match.GameScore;
import com.ephirious.model.value.match.PlayerSide;
import com.ephirious.model.value.match.StandardGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.ephirious.junit.model.EventGameBuilder.sequence;
import static com.ephirious.model.value.match.PlayerSide.FIRST;
import static com.ephirious.model.value.match.PlayerSide.SECOND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class StandardGameTest {
    private final StandardGame game = new StandardGame();

    @Test
    void shouldNewObject() {
        assertThat(game != game.pointTo(FIRST)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToUnfinishedGame")
    void shouldThrowWhenWinnerNotDefine(List<PlayerSide> pointEvents) {
        GameScore newGame = game;
        for (var side : pointEvents) {
            newGame = newGame.pointTo(side);
        }
        assertThatThrownBy(newGame::winner).isInstanceOfAny(
                IllegalStateException.class,
                UnsupportedOperationException.class
        );
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinnerFirst")
    void shouldWinnerFirst(List<PlayerSide> pointEvents) {
        GameScore newGame = game;
        for (var side : pointEvents) {
            newGame = newGame.pointTo(side);
        }
        assertThat(newGame.winner()).isEqualTo(FIRST);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinnerSecond")
    void shouldWinnerSecond(List<PlayerSide> pointEvents) {
        GameScore newGame = game;
        for (var side : pointEvents) {
            newGame = newGame.pointTo(side);
        }
        assertThat(newGame.winner()).isEqualTo(SECOND);
    }

    private static Stream<List<PlayerSide>> providePointEventsToUnfinishedGame() {
        return Stream.of(
                sequence(),
                sequence(FIRST, 1),
                sequence(SECOND, 1),
                sequence(FIRST, 1, SECOND, 1),
                sequence(FIRST, 3, SECOND, 2),
                sequence(SECOND, 3, FIRST, 2),
                sequence(FIRST, 3, SECOND, 3),
                sequence(FIRST, 3, SECOND, 3, FIRST, 1, SECOND, 1),
                sequence(FIRST, 3, SECOND, 3, FIRST, 1, SECOND, 1, FIRST, 1, SECOND, 2),
                sequence(FIRST, 3, SECOND, 3, FIRST, 1, SECOND, 1, SECOND, 1, FIRST, 2)
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToWinnerFirst() {
        return Stream.of(
                sequence(FIRST, 4),
                sequence(FIRST, 3, SECOND, 3, FIRST, 2),
                sequence(FIRST, 3, SECOND, 3, FIRST, 1, SECOND, 1, SECOND, 1, FIRST, 2, FIRST, 1)
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToWinnerSecond() {
        return Stream.of(
                sequence(SECOND, 4),
                sequence(SECOND, 3, FIRST, 3, SECOND, 2),
                sequence(SECOND, 3, FIRST, 3, SECOND, 1, FIRST, 1, FIRST, 1, SECOND, 2, SECOND, 1)
        );
    }
}
