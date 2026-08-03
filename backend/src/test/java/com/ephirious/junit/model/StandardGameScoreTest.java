package com.ephirious.junit.model;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.junit.util.PointChainBuilder;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import com.ephirious.model.value.score.game.StandardGameScore;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.ephirious.junit.util.PointChainBuilder.build;
import static com.ephirious.model.value.score.PlayerSide.FIRST;
import static com.ephirious.model.value.score.PlayerSide.SECOND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class StandardGameScoreTest {
    private final StandardGameScore game = new StandardGameScore();

    @ParameterizedTest
    @EnumSource(PlayerSide.class)
    void shouldNewObject(PlayerSide side) {
        assertThat(game != game.pointTo(side)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestFirstPoints")
    void shouldGetCorrectFirstPoint(List<PlayerSide> pointChain, String expected) {
        Score<?> resultGame = PointChainBuilder.awardedGame(game, pointChain);
        assertThat(resultGame.firstPlayerScore()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestSecondPoints")
    void shouldGetCorrectSecondPoint(List<PlayerSide> pointChain, String expected) {
        Score<?> result = PointChainBuilder.awardedGame(game, pointChain);
        assertThat(result.secondPlayerScore()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideStandardGameWithWinner")
    void shouldWinExpectedPlayer(List<PlayerSide> pointChain, PlayerSide expectedWinner) {
        Score<?> result = PointChainBuilder.awardedGame(game, pointChain);
        assertThat(result.winner()).isEqualTo(expectedWinner);
    }

    @ParameterizedTest
    @MethodSource("provideGameAfterWinnerFound")
    void throwExceptionIfGameIsOver(List<PlayerSide> pointChain) {
        assertThatThrownBy(() -> PointChainBuilder.awardedGame(game, pointChain))
                .isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("provideNotWinnerGame")
    void throwExceptionIfGameNotOver(List<PlayerSide> pointChain) {
        Score<?> newGame = PointChainBuilder.awardedGame(game, pointChain);
        assertThatThrownBy(newGame::winner).isInstanceOf(ContractViolationException.class);
    }

    private static Stream<Arguments> providePointChainForTestFirstPoints() {
        return Stream.of(
                Arguments.of(build(""), "0"),
                Arguments.of(build("f1"), "15"),
                Arguments.of(build("f2"), "30"),
                Arguments.of(build("f3"), "40")
        );
    }

    private static Stream<Arguments> providePointChainForTestSecondPoints() {
        return Stream.of(
                Arguments.of(build(""), "0"),
                Arguments.of(build("s1"), "15"),
                Arguments.of(build("s2"), "30"),
                Arguments.of(build("s3"), "40")
        );
    }

    private static Stream<Arguments> provideStandardGameWithWinner() {
        return Stream.of(
                Arguments.of(build("f4"), FIRST),
                Arguments.of(build("s4"), SECOND),
                Arguments.of(build("f3 s3 f2"), FIRST),
                Arguments.of(build("s3 f3 s2"), SECOND),
                Arguments.of(build("f3 s3 f1 s1 f2"), FIRST),
                Arguments.of(build("s3 f3 s1 f1 s2"), SECOND)
        );
    }

    private static Stream<Arguments> provideGameAfterWinnerFound() {
        return Stream.of(
                Arguments.of(build("f4 s1")),
                Arguments.of(build("s4 f1")),
                Arguments.of(build("f3 s3 f2 f1")),
                Arguments.of(build("s3 f3 s2 f1")),
                Arguments.of(build("f3 s3 f1 s1 f2 s1")),
                Arguments.of(build("s3 f3 s1 f1 s2 f1"))
        );
    }

    private static Stream<Arguments> provideNotWinnerGame() {
        return Stream.of(
                Arguments.of(build("")),
                Arguments.of(build("f1")),
                Arguments.of(build("s1")),
                Arguments.of(build("f2")),
                Arguments.of(build("s2")),
                Arguments.of(build("f3")),
                Arguments.of(build("s3"))
        );
    }
}
