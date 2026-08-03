package com.ephirious.model.value.score.game;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;

public class TieBreakGameScore extends AbstractGameScore<Integer> {
    private static final int DIFFERENCE_TO_WIN = 2;

    private final int firstPoints;
    private final int secondPoints;
    private final TieBreakGameType type;

    public TieBreakGameScore(TieBreakGameType type) {
        this.firstPoints = this.secondPoints = 0;
        this.type = type;
    }

    private TieBreakGameScore(int first, int second, TieBreakGameType type) {
        this.firstPoints = first;
        this.secondPoints = second;
        this.type = type;
    }

    @Override
    public boolean hasWinner() {
        return isWin();
    }

    @Override
    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new ContractViolationException("Can't get game's winner, because games isn't over");
        }
        return firstPoints > secondPoints ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    @Override
    public AbstractGameScore<Integer> pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new ContractViolationException("Can't increase game score, because game is over");
        }
        if (side == PlayerSide.FIRST) {
            return new TieBreakGameScore(firstPoints + 1, secondPoints, type);
        }
        return new TieBreakGameScore(firstPoints, secondPoints + 1, type);
    }

    @Override
    public Integer firstPlayerScore() {
        return firstPoints;
    }

    @Override
    public Integer secondPlayerScore() {
        return secondPoints;
    }

    private boolean isWin() {
        return Math.max(firstPoints, secondPoints) >= type.score() &&
               Math.abs(firstPoints - secondPoints) >= DIFFERENCE_TO_WIN;
    }
}
