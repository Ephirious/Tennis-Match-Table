package com.ephirious.model.value.match;

import com.ephirious.exception.domain.ContractViolationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchScore {
    private final int firstSetPoints;
    private final int secondSetPoints;
    private final SetScore currentSet;

    public MatchScore() {
        firstSetPoints = secondSetPoints = 0;
        currentSet = new SetScore();
    }

    public MatchScore pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new ContractViolationException("Can't increase match score, because match is over");
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
            throw new ContractViolationException("Can't get match's winner, because match isn't over");
        }
        return firstSetPoints == 2 ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    public int firstPlayerSetPoint() {
        return firstSetPoints;
    }

    public int secondPlayerSetPoint() {
        return secondSetPoints;
    }

    public SetScore currentSet() {
        return currentSet;
    }
}
