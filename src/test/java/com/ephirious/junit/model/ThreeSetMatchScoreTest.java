package com.ephirious.junit.model;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import com.ephirious.model.value.score.match.ThreeSetMatchScore;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.ephirious.junit.util.PointChainBuilder.awardedGame;
import static com.ephirious.junit.util.PointChainBuilder.build;
import static com.ephirious.model.value.score.PlayerSide.FIRST;
import static com.ephirious.model.value.score.PlayerSide.SECOND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ThreeSetMatchScoreTest {
    private final ThreeSetMatchScore match = new ThreeSetMatchScore();

    @ParameterizedTest
    @EnumSource(PlayerSide.class)
    void shouldNewObject(PlayerSide side) {
        assertThat(match.pointTo(side)).isNotEqualTo(match);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestFirstScore")
    void shouldGetExpectedFirstPlayerScore(List<PlayerSide> pointChain, int expected) {
        Score<?> score = awardedGame(match, pointChain);
        assertThat(score.firstPlayerScore()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestSecondScore")
    void shouldGetExpectedSecondPlayerScore(List<PlayerSide> pointChain, int expected) {
        Score<?> score = awardedGame(match, pointChain);
        assertThat(score.secondPlayerScore()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("providePointCharinNotWinnerExist")
    void throwExceptionWhenWinnerNotExists(List<PlayerSide> pointChain) {
        Score<?> score = awardedGame(match, pointChain);
        assertThatThrownBy(score::winner).isInstanceOf(ContractViolationException.class);

    }

    @ParameterizedTest
    @MethodSource("providePointChainWithExpectedWinner")
    void shouldWinExpectedPlayer(List<PlayerSide> pointChain, PlayerSide expectedWinner) {

    }

    private static Stream<Arguments> providePointChainForTestFirstScore() {
        return Stream.of(
                Arguments.of(build(""), 0),
                Arguments.of(build("f23"), 0),
                Arguments.of(build("f24"), 1),
                Arguments.of(build("f47"), 1),
                Arguments.of(build("f48"), 2)
        );
    }

    private static Stream<Arguments> providePointChainForTestSecondScore() {
        return Stream.of(
                Arguments.of(build(""), 0),
                Arguments.of(build("s23"), 0),
                Arguments.of(build("s24"), 1),
                Arguments.of(build("s47"), 1),
                Arguments.of(build("s48"), 2)
        );
    }

    private static Stream<List<PlayerSide>> providePointCharinNotWinnerExist() {
        List<List<PlayerSide>> result = new ArrayList<>();
        int winScore = 48;
        for (int i = 0; i < winScore; i++) {
            result.add(build("f%d".formatted(i)));
            result.add(build("s%d".formatted(i)));
        }
        return result.stream();
    }

    private static Stream<Arguments> providePointChainWithExpectedWinner() {
        return Stream.of(
                Arguments.of(build("f48"), FIRST),
                Arguments.of(build("s48"), SECOND),
                Arguments.of(build("f24 s24 f24"), FIRST),
                Arguments.of(build("s24 f24 s24"), SECOND),
                Arguments.of(build("f24 s24 f20 s20 f4 s4 f7"), FIRST),
                Arguments.of(build("s24 f24 s20 f20 s4 f4 s7"), SECOND)
        );
    }
}
