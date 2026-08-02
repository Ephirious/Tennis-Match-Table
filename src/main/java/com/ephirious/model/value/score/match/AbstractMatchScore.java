package com.ephirious.model.value.score.match;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.Score;
import com.ephirious.model.value.score.set.AbstractSetScore;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractMatchScore implements Score<Integer> {
    @Getter(AccessLevel.PROTECTED)
    private final int firstSetPoints;

    @Getter(AccessLevel.PROTECTED)
    private final int secondSetPoints;

    @Getter(AccessLevel.PROTECTED)
    private final AbstractSetScore currentSet;

    private final int winScore;

    protected AbstractMatchScore(AbstractSetScore setScore, int winScore) {
        this.firstSetPoints = this.secondSetPoints = 0;
        this.winScore = winScore;
        this.currentSet = setScore;
    }

    protected AbstractMatchScore(int first, int second, AbstractSetScore currentSet, int winScore) {
        this.firstSetPoints = first;
        this.secondSetPoints = second;
        this.winScore = winScore;
        this.currentSet = currentSet;
    }

    protected abstract AbstractMatchScore provideWinnerSetScore(AbstractSetScore newSet);

    protected abstract AbstractMatchScore provideDefaultSetScore(AbstractSetScore newSet);

    public AbstractMatchScore pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new ContractViolationException("Can't increase match score, because match is over");
        }

        AbstractSetScore newSet = getCurrentSet().pointTo(side);
        if (newSet.hasWinner()) {
            return provideWinnerSetScore(newSet);
        }

        return provideDefaultSetScore(newSet);
    }

    @Override
    public boolean hasWinner() {
        return firstSetPoints == winScore || secondSetPoints == winScore;
    }

    @Override
    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new ContractViolationException("Can't get match's winner, because match isn't over");
        }
        return firstSetPoints == winScore ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    @Override
    public Integer firstPlayerScore() {
        return firstSetPoints;
    }

    @Override
    public Integer secondPlayerScore() {
        return secondSetPoints;
    }

    public AbstractSetScore currentSet() {
        return currentSet;
    }
}
