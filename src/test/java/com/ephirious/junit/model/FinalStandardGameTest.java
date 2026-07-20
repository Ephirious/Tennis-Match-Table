package com.ephirious.junit.model;

import com.ephirious.model.value.match.FinalGameState;
import com.ephirious.model.value.match.FinalStandardGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.ephirious.model.value.match.PlayerSide.FIRST;
import static com.ephirious.model.value.match.PlayerSide.SECOND;
import static org.assertj.core.api.AssertionsForClassTypes.*;

class FinalStandardGameTest {
    private final FinalStandardGame game = new FinalStandardGame(FinalGameState.DEUCE);

    @ParameterizedTest
    @MethodSource("providedNotWinnerState")
    void shouldThrowWhenWinnerNotDefined(FinalStandardGame game) {
        assertThatThrownBy(game::winner).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldNewObject() {
        assertThat(game != game.pointTo(FIRST)).isTrue();
    }

    @Test
    void shouldAdvantageFirst() {
        FinalStandardGame newGame = game.pointTo(FIRST);
        assertThat(newGame.isAdvantageFirst()).isTrue();
    }

    @Test
    void shouldAdvantageSecond() {
        FinalStandardGame newGame = game.pointTo(SECOND);
        assertThat(newGame.isAdvantageSecond()).isTrue();
    }

    @Test
    void shouldWinFirst() {
        FinalStandardGame newGame = game.pointTo(FIRST).pointTo(FIRST);
        assertThat(newGame.winner()).isEqualTo(FIRST);
    }

    @Test
    void shouldWinSecond() {
        FinalStandardGame newGame = game.pointTo(SECOND).pointTo(SECOND);
        assertThat(newGame.winner()).isEqualTo(SECOND);
    }

    @Test
    void shouldWillBeDeuceWhenFirstPlayerGetFirstAdvantage() {
        FinalStandardGame newGame = game.pointTo(FIRST).pointTo(SECOND);
        assertThat(newGame.isDeuce()).isTrue();
    }

    @Test
    void shouldWillBeDeuceWhenSecondPlayerGetFirstAdvantage() {
        FinalStandardGame newGame = game.pointTo(SECOND).pointTo(FIRST);
        assertThat(newGame.isDeuce()).isTrue();
    }

    @Test
    void shouldWinFirstAfterAdvantageSecond() {
        FinalStandardGame newGame = game.pointTo(SECOND).pointTo(FIRST).pointTo(FIRST).pointTo(FIRST);
        assertThat(newGame.winner()).isEqualTo(FIRST);
    }

    @Test
    void shouldWinSecondAfterAdvantageFirst() {
        FinalStandardGame newGame = game.pointTo(FIRST).pointTo(SECOND).pointTo(SECOND).pointTo(SECOND);
        assertThat(newGame.winner()).isEqualTo(SECOND);
    }

    private static Stream<FinalStandardGame> providedNotWinnerState() {
        return Stream.of(
                new FinalStandardGame(FinalGameState.DEUCE),
                new FinalStandardGame(FinalGameState.AD_SECOND),
                new FinalStandardGame(FinalGameState.AD_FIRST)
        );
    }
}
