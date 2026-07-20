package com.ephirious.junit.model;

import com.ephirious.model.value.match.MatchScore;
import com.ephirious.model.value.match.PlayerSide;
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

class MatchScoreTest {
    private final MatchScore score = new MatchScore();

    @Test
    void shouldNewObject() {
        assertThat(score != score.pointTo(FIRST)).isTrue();
    }

    @Test
    void shouldThrowWhenWinnerNotDefined() {
        assertThatThrownBy(score::winner).isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinFirst")
    void shouldWinnerFirst(List<PlayerSide> pointEvents) {
        MatchScore newMatchScore = score;
        for (var side : pointEvents) {
            newMatchScore = newMatchScore.pointTo(side);
        }
        assertThat(newMatchScore.winner()).isEqualTo(FIRST);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinSecond")
    void shouldWinnerSecond(List<PlayerSide> pointEvents) {
        MatchScore newMatchScore = score;
        for (var side : pointEvents) {
            newMatchScore = newMatchScore.pointTo(side);
        }
        assertThat(newMatchScore.winner()).isEqualTo(SECOND);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToUnfinishedSet")
    void shouldUnfinishedGame(List<PlayerSide> pointEvents) {
        MatchScore newMatchScore = score;
        for (var side : pointEvents) {
            newMatchScore = newMatchScore.pointTo(side);
        }
        assertThat(newMatchScore.hasWinner()).isFalse();
    }



    private static Stream<List<PlayerSide>> providePointEventsToWinFirst() {
        return Stream.of(
                sequence(
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, FIRST, 4,
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, FIRST, 4
                ),
                sequence(
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, FIRST, 4,
                        SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 4,
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, FIRST, 4
                ),
                sequence(
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 7,
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 7
                )
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToWinSecond() {
        return Stream.of(
                sequence(
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 4, SECOND, 4,
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 4, SECOND, 4
                ),
                sequence(
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 4, SECOND, 4,
                        SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, FIRST, 4, FIRST, 4,
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 4, SECOND, 4
                ),
                sequence(
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 7,
                        FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, SECOND, 7
                )
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToUnfinishedSet() {
        return Stream.of(
                sequence(),
                sequence(FIRST, 47, SECOND, 47)
        );
    }
}
