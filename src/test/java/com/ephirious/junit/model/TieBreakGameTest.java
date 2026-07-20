package com.ephirious.junit.model;

import com.ephirious.model.value.match.PlayerSide;
import com.ephirious.model.value.match.TieBreakGame;
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


public class TieBreakGameTest {
    private final TieBreakGame game = new TieBreakGame();


    @Test
    void shouldNewObject() {
        assertThat(game != game.pointTo(FIRST)).isTrue();
    }

    @Test
    void shouldThrowWhenWinnerNotDefined() {
        assertThatThrownBy(game::winner).isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinFirst")
    void shouldWinnerFirst(List<PlayerSide> pointEvents) {
        TieBreakGame newGame = game;
        for (var side : pointEvents) {
            newGame = newGame.pointTo(side);
        }
        assertThat(newGame.winner()).isEqualTo(FIRST);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinSecond")
    void shouldWinnerSecond(List<PlayerSide> pointEvents) {
        TieBreakGame newGame = game;
        for (var side : pointEvents) {
            newGame = newGame.pointTo(side);
        }
        assertThat(newGame.winner()).isEqualTo(SECOND);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToUnfinishedGame")
    void shouldUnfinishedGame(List<PlayerSide> pointEvents) {
        TieBreakGame newGame = game;
        for (var side : pointEvents) {
            newGame = newGame.pointTo(side);
        }
        assertThat(newGame.hasWinner()).isFalse();
    }



    private static Stream<List<PlayerSide>> providePointEventsToWinFirst() {
        return Stream.of(
                sequence(FIRST, 7),
                sequence(FIRST, 5, SECOND, 5, FIRST, 2),
                sequence(FIRST, 5, SECOND, 5, FIRST, 1, SECOND, 1, FIRST, 2),
                sequence(FIRST, 5, SECOND, 5, FIRST, 1, SECOND, 1, FIRST, 1, SECOND, 1, FIRST, 2)
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToWinSecond() {
        return Stream.of(
                sequence(SECOND, 7),
                sequence(SECOND, 5, FIRST, 5, SECOND, 2),
                sequence(SECOND, 5, FIRST, 5, SECOND, 1, FIRST, 1, SECOND, 2),
                sequence(SECOND, 5, FIRST, 5, SECOND, 1, FIRST, 1, SECOND, 1, FIRST, 1, SECOND, 2)

        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToUnfinishedGame() {
        return Stream.of(
                sequence(FIRST, 1),
                sequence(SECOND, 1),
                sequence(FIRST, 6),
                sequence(SECOND, 6),
                sequence(FIRST, 5, SECOND, 6),
                sequence(SECOND, 5, FIRST, 6),
                sequence(FIRST, 6, SECOND, 6),
                sequence(SECOND, 6, FIRST, 5)
        );
    }
}
