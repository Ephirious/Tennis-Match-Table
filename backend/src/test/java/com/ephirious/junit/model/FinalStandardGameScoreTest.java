package com.ephirious.junit.model;


import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.game.FinalGameState;
import com.ephirious.model.value.score.game.FinalStandardGameScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.ephirious.model.value.score.PlayerSide.FIRST;
import static com.ephirious.model.value.score.PlayerSide.SECOND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FinalStandardGameScoreTest {
    private final FinalStandardGameScore game = new FinalStandardGameScore(FinalGameState.DEUCE);

    @ParameterizedTest
    @MethodSource("providedNotWinnerState")
    void shouldThrowWhenWinnerNotDefined(FinalStandardGameScore game) {
        assertThatThrownBy(game::winner).isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @EnumSource(PlayerSide.class)
    void shouldNewObject(PlayerSide side) {
        assertThat(game != game.pointTo(side)).isTrue();
    }

    @Test
    void shouldAdvantageFirst() {
        FinalStandardGameScore newGame = game.pointTo(FIRST);
        assertThat(newGame.isAdvantageFirst()).isTrue();
    }

    @Test
    void shouldAdvantageSecond() {
        FinalStandardGameScore newGame = game.pointTo(SECOND);
        assertThat(newGame.isAdvantageSecond()).isTrue();
    }

    @Test
    void shouldWinFirst() {
        FinalStandardGameScore newGame = game.pointTo(FIRST).pointTo(FIRST);
        assertThat(newGame.winner()).isEqualTo(FIRST);
    }

    @Test
    void shouldWinSecond() {
        FinalStandardGameScore newGame = game.pointTo(SECOND).pointTo(SECOND);
        assertThat(newGame.winner()).isEqualTo(SECOND);
    }

    @Test
    void shouldWillBeDeuceWhenFirstPlayerGetFirstAdvantage() {
        FinalStandardGameScore newGame = game.pointTo(FIRST).pointTo(SECOND);
        assertThat(newGame.isDeuce()).isTrue();
    }

    @Test
    void shouldWillBeDeuceWhenSecondPlayerGetFirstAdvantage() {
        FinalStandardGameScore newGame = game.pointTo(SECOND).pointTo(FIRST);
        assertThat(newGame.isDeuce()).isTrue();
    }

    @Test
    void shouldWinFirstAfterAdvantageSecond() {
        FinalStandardGameScore newGame = game.pointTo(SECOND).pointTo(FIRST).pointTo(FIRST).pointTo(FIRST);
        assertThat(newGame.winner()).isEqualTo(FIRST);
    }

    @Test
    void shouldWinSecondAfterAdvantageFirst() {
        FinalStandardGameScore newGame = game.pointTo(FIRST).pointTo(SECOND).pointTo(SECOND).pointTo(SECOND);
        assertThat(newGame.winner()).isEqualTo(SECOND);
    }

    @ParameterizedTest
    @MethodSource("provideStateForCheckFirstPoints")
    void shouldGetCorrectFirstPointValue(FinalGameState state, String expected) {
        FinalStandardGameScore game = new FinalStandardGameScore(state);
        assertThat(game.firstPlayerScore()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideStateForCheckSecondPoints")
    void shouldGetCorrectSecondPointValue(FinalGameState state, String expected) {
        FinalStandardGameScore game = new FinalStandardGameScore(state);
        assertThat(game.secondPlayerScore()).isEqualTo(expected);
    }


    private static Stream<FinalStandardGameScore> providedNotWinnerState() {
        return Stream.of(
                new FinalStandardGameScore(FinalGameState.DEUCE),
                new FinalStandardGameScore(FinalGameState.AD_SECOND),
                new FinalStandardGameScore(FinalGameState.AD_FIRST)
        );
    }

    private static Stream<Arguments> provideStateForCheckFirstPoints() {
        return Stream.of(
                Arguments.of(FinalGameState.DEUCE, "40"),
                Arguments.of(FinalGameState.AD_FIRST, "AD"),
                Arguments.of(FinalGameState.AD_SECOND, "40"),
                Arguments.of(FinalGameState.WIN_FIRST, "0"),
                Arguments.of(FinalGameState.WIN_SECOND, "0")
        );
    }

    private static Stream<Arguments> provideStateForCheckSecondPoints() {
        return Stream.of(
                Arguments.of(FinalGameState.DEUCE, "40"),
                Arguments.of(FinalGameState.AD_FIRST, "40"),
                Arguments.of(FinalGameState.AD_SECOND, "AD"),
                Arguments.of(FinalGameState.WIN_FIRST, "0"),
                Arguments.of(FinalGameState.WIN_SECOND, "0")
        );
    }
}
