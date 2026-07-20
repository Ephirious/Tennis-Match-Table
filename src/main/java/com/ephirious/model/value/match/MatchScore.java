package com.ephirious.model.value.match;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchScore {
    private final int firstSetPoints;
    private final int secondSetPoints;
    private final SetScore currentSet;

    public MatchScore pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new IllegalStateException("Winner already exists");
        }

        SetScore newSet = currentSet.pointTo(side);
        if (newSet.hasWinner()) {
            int newFirst = newSet.winner() == PlayerSide.FIRST ? firstSetPoints + 1 : firstSetPoints;
            int newSecond = newSet.winner() == PlayerSide.SECOND ? secondSetPoints + 1 : secondSetPoints;
            return new MatchScore(newFirst, newSecond, new SetScore());
        }
        return new MatchScore(firstSetPoints, secondSetPoints, newSet);
    }

    public boolean hasWinner() {
        return firstSetPoints == 2 || secondSetPoints == 2;
    }

    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new IllegalStateException("The match's winner didn't define");
        }
        return firstSetPoints == 2 ? PlayerSide.FIRST : PlayerSide.SECOND;
    }
}
