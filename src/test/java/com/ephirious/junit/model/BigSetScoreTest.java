package com.ephirious.junit.model;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import com.ephirious.model.value.score.set.BigSetScore;
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

public class BigSetScoreTest {
    private final BigSetScore set = new BigSetScore();

    @ParameterizedTest
    @EnumSource(PlayerSide.class)
    void shouldGetNewObject(PlayerSide side) {
        assertThat(set.pointTo(side)).isNotEqualTo(set);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestFirstPoints")
    void shouldCorrectScoreFirstPlayer(List<PlayerSide> pointChain, int expectedScore) {
        Score<?> score = awardedGame(set, pointChain);
        assertThat(score.firstPlayerScore()).isEqualTo(expectedScore);
    }

    @ParameterizedTest
    @MethodSource("providePointChainForTestSecondPoints")
    void shouldCorrectScoreSecondPlayer(List<PlayerSide> pointChain, int expectedScore) {
        Score<?> score = awardedGame(set, pointChain);
        assertThat(score.secondPlayerScore()).isEqualTo(expectedScore);
    }

    @ParameterizedTest
    @MethodSource("providePointChainNotWinner")
    void throwExceptionIfWinnerNotExist(List<PlayerSide> pointChain) {
        Score<?> score = awardedGame(set, pointChain);
        assertThatThrownBy(score::winner).isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("providePointChainWithWinner")
    void shouldExpectedWinner(List<PlayerSide> pointChain, PlayerSide expectedPlayer) {
        Score<?> score = awardedGame(set, pointChain);
        assertThat(score.winner()).isEqualTo(expectedPlayer);
    }

    private static Stream<Arguments> providePointChainForTestFirstPoints() {
        return Stream.of(
                Arguments.of(build(""), 0),
                Arguments.of(build("f4"), 1),
                Arguments.of(build("f8"), 2),
                Arguments.of(build("f12"), 3),
                Arguments.of(build("f16"), 4),
                Arguments.of(build("f20"), 5),
                Arguments.of(build("f24"), 6),
                Arguments.of(build("f20 s20 f4 s4 f10"), 7)
        );
    }

    private static Stream<Arguments> providePointChainForTestSecondPoints() {
        return Stream.of(
                Arguments.of(build(""), 0),
                Arguments.of(build("s4"), 1),
                Arguments.of(build("s8"), 2),
                Arguments.of(build("s12"), 3),
                Arguments.of(build("s16"), 4),
                Arguments.of(build("s20"), 5),
                Arguments.of(build("s24"), 6),
                Arguments.of(build("s20 f20 s4 f4 s10"), 7)
        );
    }

    private static Stream<List<PlayerSide>> providePointChainNotWinner() {
        List<List<PlayerSide>> result = new ArrayList<>();
        int winScore = 24;
        for (int i = 0; i < winScore; i++) {
            result.add(build("f%d".formatted(i)));
            result.add(build("s%d".formatted(i)));
        }
        return result.stream();
    }

    private static Stream<Arguments> providePointChainWithWinner() {
        return Stream.of(
                Arguments.of(build("f24"), FIRST),
                Arguments.of(build("s24"), SECOND),
                Arguments.of(build("f16 s16 f8"), FIRST),
                Arguments.of(build("s16 f16 s8"), SECOND),
                Arguments.of(build("f20 s20 f8"), FIRST),
                Arguments.of(build("s20 f20 s8"), SECOND),
                Arguments.of(build("f20 s20 f4 s4 f10"), FIRST),
                Arguments.of(build("s20 f20 s4 f4 s10"), SECOND)
        );
    }
}
