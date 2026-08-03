package com.ephirious.junit.util;

import com.ephirious.junit.exception.InvalidPointPatternException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class PointChainBuilder {
    private static final char FIRST_PLAYER_CHARACTER = 'f';
    private static final char SECOND_PLAYER_CHARACTER = 's';
    private static final int INDEX_PLAYER_SIDE = 0;


    public static Score<?> awardedGame(Score<?> score,List<PlayerSide> pointChain) {
        Score<?> newScore = score;
        for (PlayerSide side : pointChain) {
            newScore = newScore.pointTo(side);
        }
        return newScore;
    }

    public static List<PlayerSide> build(@NonNull String pattern) {
        if (pattern.isBlank()) {
            return List.of();
        }
        checkPatternCorrectness(pattern);
        return Arrays.stream(pattern.split(" "))
                .map(PointChainBuilder::divideAtomicPattern)
                .flatMap(List::stream)
                .toList();
    }

    private static void checkPatternCorrectness(String pattern) {
        Arrays.stream(pattern.split(" "))
                .filter(point -> !isCorrectAtomicPointPattern(point))
                .findFirst()
                .ifPresent(invalidPoint -> {
                    throw new InvalidPointPatternException("Invalid point: %s".formatted(invalidPoint));
                });

    }

    private static boolean isCorrectAtomicPointPattern(String atomic) {
        char side = Character.toLowerCase(atomic.charAt(INDEX_PLAYER_SIDE));
        String number = atomic.substring(INDEX_PLAYER_SIDE + 1);

        boolean isSideSymbolCorrect = (side == FIRST_PLAYER_CHARACTER) || (side == SECOND_PLAYER_CHARACTER);
        boolean isNumberSideCorrect = number.codePoints().allMatch(Character::isDigit);

        return isSideSymbolCorrect && isNumberSideCorrect;
    }

    private static List<PlayerSide> divideAtomicPattern(String atomic) {
        char side = atomic.charAt(INDEX_PLAYER_SIDE);
        int number = Integer.parseInt(atomic.substring(INDEX_PLAYER_SIDE + 1));
        if (side == FIRST_PLAYER_CHARACTER) {
            return Collections.nCopies(number, PlayerSide.FIRST).stream().toList();
        }
        return Collections.nCopies(number, PlayerSide.SECOND).stream().toList();
    }
}
