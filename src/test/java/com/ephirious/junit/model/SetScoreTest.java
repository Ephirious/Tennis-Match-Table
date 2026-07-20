package com.ephirious.junit.model;

import com.ephirious.model.value.match.PlayerSide;
import com.ephirious.model.value.match.SetScore;
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

class SetScoreTest {
    private final SetScore score = new SetScore();

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
        SetScore newSet = score;
        for (var side : pointEvents) {
            newSet = newSet.pointTo(side);
        }
        assertThat(newSet.winner()).isEqualTo(FIRST);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToWinSecond")
    void shouldWinnerSecond(List<PlayerSide> pointEvents) {
        SetScore newSet = score;
        for (var side : pointEvents) {
            newSet = newSet.pointTo(side);
        }
        assertThat(newSet.winner()).isEqualTo(SECOND);
    }

    @ParameterizedTest
    @MethodSource("providePointEventsToUnfinishedSet")
    void shouldUnfinishedGame(List<PlayerSide> pointEvents) {
        SetScore newSet = score;
        for (var side : pointEvents) {
            newSet = newSet.pointTo(side);
        }
        assertThat(newSet.hasWinner()).isFalse();
    }



    private static Stream<List<PlayerSide>> providePointEventsToWinFirst() {
        return Stream.of(
                sequence(FIRST, 24),
                sequence(FIRST, 16, SECOND, 16, FIRST, 8),
                sequence(FIRST, 20, SECOND, 20, FIRST, 8),
                sequence(FIRST, 20, SECOND, 20, FIRST, 4, SECOND, 4, FIRST, 7)
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToWinSecond() {
        return Stream.of(
                sequence(SECOND, 24),
                sequence(SECOND, 16, FIRST, 16, SECOND, 8),
                sequence(SECOND, 20, FIRST, 20, SECOND, 8),
                sequence(SECOND, 20, FIRST, 20, SECOND, 4, FIRST, 4, SECOND, 7)
        );
    }

    private static Stream<List<PlayerSide>> providePointEventsToUnfinishedSet() {
        return Stream.of(
                sequence(FIRST, 20),
                sequence(FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4),
                sequence(FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4),
                sequence(FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4),
                sequence(FIRST, 12, FIRST, 2, SECOND, 2),
                sequence(FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 5, SECOND, 4),
                sequence(FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 4, SECOND, 4, FIRST, 6, SECOND, 6, FIRST, 1)

        );
    }
}
