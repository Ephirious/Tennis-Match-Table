package com.ephirious.model.value.score.set;

import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.game.AbstractGameScore;
import com.ephirious.model.value.score.game.StandardGameScore;
import com.ephirious.model.value.score.game.TieBreakGameScore;
import com.ephirious.model.value.score.game.TieBreakGameType;

public class StandardSetScore extends AbstractSetScore {
    public StandardSetScore() {
        super(new StandardGameScore());
    }

    private StandardSetScore(int first, int second, AbstractGameScore<?> currentGame) {
        super(first, second, currentGame);
    }

    @Override
    protected AbstractSetScore provideWinnerSetScore(AbstractGameScore<?> newGame) {
        int newFirst = newGame.winner() == PlayerSide.FIRST
                ? getFirstGamePoints() + 1
                : getFirstGamePoints();
        int newSecond = newGame.winner() == PlayerSide.SECOND
                ? getSecondGamePoints() + 1
                : getSecondGamePoints();

        if (newFirst == WIN_SCORE && newSecond == WIN_SCORE) {
            return new StandardSetScore(newFirst, newSecond, new TieBreakGameScore(TieBreakGameType.DEFAULT));
        }
        return new StandardSetScore(newFirst, newSecond, new StandardGameScore());
    }

    @Override
    protected AbstractSetScore provideDefaultSetScore(AbstractGameScore<?> newGame) {
        return new StandardSetScore(getFirstGamePoints(), getSecondGamePoints(), newGame);
    }
}
