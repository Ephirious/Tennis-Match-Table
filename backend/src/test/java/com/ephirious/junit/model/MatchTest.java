package com.ephirious.junit.model;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.exception.domain.UnknowWhichPlayerAwardPointException;
import com.ephirious.model.aggregate.Match;
import com.ephirious.model.aggregate.MatchType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.ephirious.model.aggregate.MatchType.BEST_OF_FIVE;
import static com.ephirious.model.aggregate.MatchType.BEST_OF_THREE;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MatchTest {

    @ParameterizedTest
    @MethodSource("provideNullIds")
    void throwNullPointerException(UUID first, UUID second, MatchType type) {
        assertThatThrownBy(() -> new Match(first, second, type)).isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @MethodSource("provideIdsWithFakeId")
    void throwExceptionWhenTargetIdIsNotOneOfPlayerId(UUID first, UUID second, UUID fake, MatchType type) {
        Match match = new Match(first, second, type);
        assertThatThrownBy(() -> match.pointTo(fake)).isInstanceOf(UnknowWhichPlayerAwardPointException.class);
    }

    @ParameterizedTest
    @MethodSource("provideNullTargetIdToAward")
    void throwExceptionWhenTargetIdIsNull(UUID target, MatchType type) {
        Match match = new Match(randomUUID(), randomUUID(), type);
        assertThatThrownBy(() -> match.pointTo(target)).isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EnumSource(MatchType.class)
    void shouldGetCorrectFirstPlayerId(MatchType type) {
        UUID id = randomUUID();
        Match match = new Match(id, randomUUID(), type);
        assertThat(match.firstPlayerId()).isEqualTo(id);
    }

    @ParameterizedTest
    @EnumSource(MatchType.class)
    void shouldGetCorrectSecondPlayerId(MatchType type) {
        UUID id = randomUUID();
        Match match = new Match(randomUUID(), id, type);
        assertThat(match.secondPlayerId()).isEqualTo(id);
    }

    @ParameterizedTest
    @MethodSource("provideMatchWithWinners")
    void shouldGetCorrectWinners(Match match, UUID winner) {
        assertThat(match.winner()).isEqualTo(winner);
    }

    @ParameterizedTest
    @MethodSource("provideMatchWithoutWinner")
    void throwExceptionWhenWinnerNotExists(Match match) {
        assertThatThrownBy(match::winner).isInstanceOf(ContractViolationException.class);
    }

    @ParameterizedTest
    @MethodSource("provideMatchWithWinners")
    void shouldMatchEnded(Match match, UUID winner) {
        assertThat(match.matchEnded()).isTrue();
    }

    private static Stream<Arguments> provideNullIds() {
        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of(null, null, BEST_OF_THREE),
                Arguments.of(null, null, BEST_OF_FIVE),
                Arguments.of(null, randomUUID(), null),
                Arguments.of(null, randomUUID(), BEST_OF_THREE),
                Arguments.of(null, randomUUID(), BEST_OF_FIVE),
                Arguments.of(randomUUID(), null, null),
                Arguments.of(randomUUID(), null, BEST_OF_THREE),
                Arguments.of(randomUUID(), null, BEST_OF_FIVE)

        );
    }

    private static Stream<Arguments> provideIdsWithFakeId() {
        return Stream.of(
                Arguments.of(randomUUID(), randomUUID(), randomUUID(), BEST_OF_THREE),
                Arguments.of(randomUUID(), randomUUID(), randomUUID(), BEST_OF_FIVE)
        );
    }

    private static Stream<Arguments> provideNullTargetIdToAward() {
        return Stream.of(
                Arguments.of(null, BEST_OF_THREE),
                Arguments.of(null, BEST_OF_FIVE)
        );
    }

    private static Stream<Arguments> provideMatchWithWinners() {
        UUID first = randomUUID();
        UUID second = randomUUID();
        return Stream.of(
                Arguments.of(awardedPoint(new Match(first, second, BEST_OF_THREE), first, 48), first),
                Arguments.of(awardedPoint(new Match(first, second, BEST_OF_THREE), second, 48), second),
                Arguments.of(awardedPoint(new Match(first, second, BEST_OF_FIVE), first, 72), first),
                Arguments.of(awardedPoint(new Match(first, second, BEST_OF_FIVE), second, 72), second)
        );
    }

    private static Match awardedPoint(Match match, UUID target, int numberOfPoints) {
        for (int i = 0; i < numberOfPoints; i++) {
            match.pointTo(target);
        }
        return match;
    }

    private static Stream<Match> provideMatchWithoutWinner() {
        UUID first = randomUUID();
        UUID second = randomUUID();
        int pointsToWinInThreeSetMatch = 48;
        int pointsToWinInFiveSetMatch = 72;

        List<Match> matches = new ArrayList<>();
        matches.addAll(matchesWithoutWinner(first, second, BEST_OF_THREE, pointsToWinInThreeSetMatch));
        matches.addAll(matchesWithoutWinner(first, second, BEST_OF_FIVE, pointsToWinInFiveSetMatch));

        return matches.stream();
    }

    private static List<Match> matchesWithoutWinner(UUID first, UUID second, MatchType type, int maxPoints) {
        List<Match> matches = new ArrayList<>(maxPoints);
        for (int i = 0; i < maxPoints; i++) {
            Match firstMatch = new Match(first, second, type);
            Match secondMatch = new Match(first, second, type);
            for (int j = 0; j < i; j++) {
                firstMatch.pointTo(first);
                secondMatch.pointTo(second);
            }
            matches.add(firstMatch);
            matches.add(secondMatch);
        }
        return matches;
    }
}
