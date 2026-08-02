package com.ephirious.model.value.score.set;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import com.ephirious.model.value.score.game.AbstractGameScore;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractSetScore implements Score<Integer> {
    protected static final int DRAW_SCORE = 5;
    protected static final int WIN_SCORE = 6;
    protected static final int WIN_SCORE_TIE_BREAK = 7;

    @Getter(AccessLevel.PROTECTED)
    private final int firstGamePoints;

    @Getter(AccessLevel.PROTECTED)
    private final int secondGamePoints;

    @Getter(AccessLevel.PROTECTED)
    private final AbstractGameScore<?> currentGame;

    public AbstractSetScore(AbstractGameScore<?> currentGame) {
        this.firstGamePoints = this.secondGamePoints = 0;
        this.currentGame = currentGame;
    }

    protected AbstractSetScore(int firstGamePoints, int secondGamePoints, AbstractGameScore<?> currentGame) {
        this.firstGamePoints = firstGamePoints;
        this.secondGamePoints = secondGamePoints;
        this.currentGame = currentGame;
    }

    protected abstract AbstractSetScore provideWinnerSetScore(AbstractGameScore<?> newGame);

    protected abstract AbstractSetScore provideDefaultSetScore(AbstractGameScore<?> newGame);

    @Override
    public boolean hasWinner() {
        return isGeneralWin() || isTiebreakWin();
    }

    @Override
    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new ContractViolationException("Can't get set's winner, because the set isn't over");
        }
        return firstGamePoints > secondGamePoints ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    @Override
    public AbstractSetScore pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new ContractViolationException("Can't increase set score, because set is over");
        }

        AbstractGameScore<?> newGame = currentGame.pointTo(side);
        if (newGame.hasWinner()) {
            return provideWinnerSetScore(newGame);
        }
        return provideDefaultSetScore(newGame);
    }

    @Override
    public Integer firstPlayerScore() {
        return firstGamePoints;
    }

    @Override
    public Integer secondPlayerScore() {
        return secondGamePoints;
    }

    public AbstractGameScore<?> currentGame() {
        return currentGame;
    }

    private boolean isGeneralWin() {
        return (firstGamePoints == WIN_SCORE && secondGamePoints < DRAW_SCORE) ||
               (secondGamePoints == WIN_SCORE && firstGamePoints < DRAW_SCORE);
    }

    private boolean isTiebreakWin() {
        return firstGamePoints == WIN_SCORE_TIE_BREAK || secondGamePoints == WIN_SCORE_TIE_BREAK;
    }
}
