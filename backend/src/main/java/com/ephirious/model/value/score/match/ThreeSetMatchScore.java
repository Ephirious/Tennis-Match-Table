package com.ephirious.model.value.score.match;

import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.set.AbstractSetScore;
import com.ephirious.model.value.score.set.StandardSetScore;

public class ThreeSetMatchScore extends AbstractMatchScore {
    private static final int WIN_SCORE = 2;

    public ThreeSetMatchScore() {
        super(new StandardSetScore(), WIN_SCORE);
    }

    private ThreeSetMatchScore(int first, int second, AbstractSetScore setScore) {
        super(first, second, setScore, WIN_SCORE);
    }

    @Override
    protected AbstractMatchScore provideWinnerSetScore(AbstractSetScore newSet) {
        int newFirst = newSet.winner() == PlayerSide.FIRST
                ? getFirstSetPoints() + 1
                : getFirstSetPoints();

        int newSecond = newSet.winner() == PlayerSide.SECOND
                ? secondPlayerScore() + 1
                : secondPlayerScore();

        return new ThreeSetMatchScore(newFirst, newSecond, new StandardSetScore());
    }

    @Override
    protected AbstractMatchScore provideDefaultSetScore(AbstractSetScore newSet) {
        return new ThreeSetMatchScore(getFirstSetPoints(), getSecondSetPoints(), newSet);
    }
}
