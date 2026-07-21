package com.ephirious.model.value.match;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StandardGame implements GameScore {
    private final StandardPointState first;
    private final StandardPointState second;

    public StandardGame() {
        first = second = StandardPointState.LOVE;
    }

    @Override
    public boolean hasWinner() {
        return false;
    }

    @Override
    public PlayerSide winner() {
        throw new UnsupportedOperationException("The method isn't supported; Follow with the contract");
    }

    @Override
    public GameScore pointTo(PlayerSide side) {
        if (isFirstWin(side)) {
            return new FinalStandardGame(FinalGameState.WIN_FIRST);
        } else if (isSecondWin(side)) {
            return new FinalStandardGame(FinalGameState.WIN_SECOND);
        } else if (isDeuce(side)) {
            return new FinalStandardGame(FinalGameState.DEUCE);
        }

        if (side == PlayerSide.FIRST) {
            return new StandardGame(first.next(), second);
        }
        return new StandardGame(first, second.next());
    }

    @Override
    public String firstPlayerPoints() {
        return first.value();
    }

    @Override
    public String secondPlayerPoints() {
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
