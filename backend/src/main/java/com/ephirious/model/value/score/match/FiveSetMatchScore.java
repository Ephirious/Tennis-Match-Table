package com.ephirious.model.value.score.match;

import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.set.AbstractSetScore;
import com.ephirious.model.value.score.set.BigSetScore;
import com.ephirious.model.value.score.set.StandardSetScore;

public class FiveSetMatchScore extends AbstractMatchScore {
    private static final int DRAW_SCORE = 2;
    private static final int WIN_SCORE = 3;

    public FiveSetMatchScore() {
        super(new StandardSetScore(), WIN_SCORE);
    }

    private FiveSetMatchScore(int first, int second, AbstractSetScore setScore) {
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

        if (newFirst == DRAW_SCORE && newSecond == DRAW_SCORE) {
            return new FiveSetMatchScore(newFirst, newSecond, new BigSetScore());
        }
        return new FiveSetMatchScore(newFirst, newSecond, new StandardSetScore());
    }

    @Override
    protected AbstractMatchScore provideDefaultSetScore(AbstractSetScore newSet) {
        return new FiveSetMatchScore(getFirstSetPoints(), getSecondSetPoints(), newSet);
    }
}
