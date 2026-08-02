package com.ephirious.junit.model;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import com.ephirious.model.value.score.game.TieBreakGameScore;
import com.ephirious.model.value.score.game.TieBreakGameType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.ephirious.junit.util.PointChainBuilder.awardedGame;
import static com.ephirious.junit.util.PointChainBuilder.build;
import static com.ephirious.model.value.score.PlayerSide.FIRST;
import static com.ephirious.model.value.score.PlayerSide.SECOND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;


public class TieBreakGameScoreTest {
    private final TieBreakGameScore defaultGame = new TieBreakGameScore(TieBreakGameType.DEFAULT);
    private final TieBreakGameScore bigGame = new TieBreakGameScore(TieBreakGameType.BIG);


    @ParameterizedTest
    @EnumSource(PlayerSide.class)
    void shouldNewObject(PlayerSide side) {
        assertSoftly(softly -> {
            assertThat(defaultGame.pointTo(side)).isNotEqualTo(defaultGame);
            assertThat(bigGame.pointTo(side)).isNotEqualTo(bigGame);
        });
    }

    @ParameterizedTest
    @MethodSource("provideNotWinnerMatchEventsDefaultGame")
    void throwExceptionIfGameNotOverDefaultGame(List<PlayerSide> pointChain) {
        Score<?> score = awardedGame(defaultGame, pointChain);
        assertThatThrownBy(score::winner).isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("provideNotWinnerMatchEventsBigGame")
    void throwExceptionIfGameNotOverBigGame(List<PlayerSide> pointChain) {
        Score<?> score = awardedGame(bigGame, pointChain);
        assertThatThrownBy(score::winner).isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("providePointChainWithExpectedWinnerDefaultGame")
    void shouldWinExpectedPlayerInDefaultGame(List<PlayerSide> pointChain, PlayerSide expectedWinner) {
        Score<?> score = awardedGame(defaultGame, pointChain);
        assertThat(score.winner()).isEqualTo(expectedWinner);
    }

    @ParameterizedTest
    @MethodSource("providePointChainWithExpectedWinnerBigGame")
    void shouldWinExpectedPlayerInBigGame(List<PlayerSide> pointChain, PlayerSide expectedWinner) {
        Score<?> score = awardedGame(bigGame, pointChain);
        assertThat(score.winner()).isEqualTo(expectedWinner);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestFirstPoints")
    void shouldGetExpectedPointsFirstPlayer(List<PlayerSide> pointChain, int expectedPoints) {
        Score<?> score = awardedGame(bigGame, pointChain);
        assertThat(score.firstPlayerScore()).isEqualTo(expectedPoints);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestSecondPoints")
    void shouldGetExpectedPointsSecondPlayer(List<PlayerSide> pointChain, int expectedPoints) {
        Score<?> score = awardedGame(bigGame, pointChain);
        assertThat(score.secondPlayerScore()).isEqualTo(expectedPoints);
    }

    private static Stream<List<PlayerSide>> provideNotWinnerMatchEventsDefaultGame() {
        return Stream.of(
                build("f1"),
                build("f2"),
                build("f3"),
                build("f4"),
                build("f5"),
                build("f6"),
                build("s1"),
                build("s2"),
                build("s3"),
                build("s4"),
                build("s5"),
                build("s6")
        );
    }

    private static Stream<List<PlayerSide>> provideNotWinnerMatchEventsBigGame() {
        return Stream.of(
                build("f1"),
                build("f2"),
                build("f3"),
                build("f4"),
                build("f5"),
                build("f6"),
                build("f7"),
                build("f8"),
                build("f9"),
                build("s1"),
                build("s2"),
                build("s3"),
                build("s4"),
                build("s5"),
                build("s6"),
                build("s7"),
                build("s8"),
                build("s9")
        );
    }

    private static Stream<Arguments> providePointChainWithExpectedWinnerDefaultGame() {
        return Stream.of(
                Arguments.of(build("f7"), FIRST),
                Arguments.of(build("s7"), SECOND),
                Arguments.of(build("s5 f7"), FIRST),
                Arguments.of(build("f5 s7"), SECOND),
                Arguments.of(build("f6 s6 f2"), FIRST),
                Arguments.of(build("s6 f6 s2"), SECOND),
                Arguments.of(build("f6 s7 f1 s1 f3"), FIRST),
                Arguments.of(build("s6 f7 s1 f1 s3"), SECOND)
        );
    }

    private static Stream<Arguments> providePointChainWithExpectedWinnerBigGame() {
        return Stream.of(
                Arguments.of(build("f10"), FIRST),
                Arguments.of(build("s10"), SECOND),
                Arguments.of(build("s8 f10"), FIRST),
                Arguments.of(build("f8 s10"), SECOND),
                Arguments.of(build("f9 s9 f2"), FIRST),
                Arguments.of(build("s9 f9 s2"), SECOND),
                Arguments.of(build("f9 s10 f1 s1 f3"), FIRST),
                Arguments.of(build("s9 f10 s1 f1 s3"), SECOND)
        );
    }

    private static Stream<Arguments> providePointChainForTestFirstPoints() {
        return Stream.of(
                Arguments.of(build(""), 0),
                Arguments.of(build("f1"), 1),
                Arguments.of(build("f2"), 2),
                Arguments.of(build("f3"), 3),
                Arguments.of(build("f4"), 4),
                Arguments.of(build("f5"), 5),
                Arguments.of(build("f6"), 6),
                Arguments.of(build("f7"), 7),
                Arguments.of(build("f8"), 8),
                Arguments.of(build("f9"), 9),
                Arguments.of(build("f10"), 10)
        );
    }

    private static Stream<Arguments> providePointChainForTestSecondPoints() {
        return Stream.of(
                Arguments.of(build(""), 0),
                Arguments.of(build("s1"), 1),
                Arguments.of(build("s2"), 2),
                Arguments.of(build("s3"), 3),
                Arguments.of(build("s4"), 4),
                Arguments.of(build("s5"), 5),
                Arguments.of(build("s6"), 6),
                Arguments.of(build("s7"), 7),
                Arguments.of(build("s8"), 8),
                Arguments.of(build("s9"), 9),
                Arguments.of(build("s10"), 10)
        );
    }
}