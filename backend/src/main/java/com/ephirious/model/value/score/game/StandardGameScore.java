package com.ephirious.model.value.score.game;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;

public class StandardGameScore extends AbstractGameScore<String> {
    private final StandardPointState first;
    private final StandardPointState second;

    public StandardGameScore() {
        first = second = StandardPointState.LOVE;
    }

    private StandardGameScore(StandardPointState first, StandardPointState second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean hasWinner() {
        return false;
    }

    @Override
    public PlayerSide winner() {
        throw new ContractViolationException("The method isn't supported; Follow with the contract");
    }

    @Override
    public AbstractGameScore<?> pointTo(PlayerSide side) {
        if (isFirstWin(side)) {
            return new FinalStandardGameScore(FinalGameState.WIN_FIRST);
        } else if (isSecondWin(side)) {
            return new FinalStandardGameScore(FinalGameState.WIN_SECOND);
        } else if (isDeuce(side)) {
            return new FinalStandardGameScore(FinalGameState.DEUCE);
        }

        if (side == PlayerSide.FIRST) {
            return new StandardGameScore(first.next(), second);
        }
        return new StandardGameScore(first, second.next());
    }

    @Override
    public String firstPlayerScore() {
        return first.value();
    }

    @Override
    public String secondPlayerScore() {
        return second.value();
    }

    private boolean isFirstWin(PlayerSide side) {
        return side == PlayerSide.FIRST && first == StandardPointState.FORTY;
    }

    private boolean isSecondWin(PlayerSide side) {
        return side == PlayerSide.SECOND && second == StandardPointState.FORTY;
    }

    private boolean isDeuce(PlayerSide side) {
        return (side == PlayerSide.SECOND && second == StandardPointState.THIRTY && first == StandardPointState.FORTY) ||
               (side == PlayerSide.FIRST && first == StandardPointState.THIRTY && second == StandardPointState.FORTY);
    }
}
