package com.ephirious.model.value.match;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TieBreakGame implements Game {
    private static final int DIFFERENCE_TO_WIN = 2;
    private static final int WIN_SCORE = 7;

    private final int firstPoints;
    private final int secondPoints;

    public TieBreakGame() {
        firstPoints = secondPoints = 0;
    }

    @Override
    public boolean hasWinner() {
        return isWin();
    }

    @Override
    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new IllegalStateException("The game's winner didn't define");
        }
        return firstPoints > secondPoints ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    @Override
    public Game pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new IllegalStateException("The game's winner already exists");
        }
        if (side == PlayerSide.FIRST) {
            return new TieBreakGame(firstPoints + 1, secondPoints);
        }
        return new TieBreakGame(firstPoints, secondPoints + 1);
    }

    private boolean isWin() {
        return Math.max(firstPoints, secondPoints) >= WIN_SCORE &&
               Math.abs(firstPoints - secondPoints) >= DIFFERENCE_TO_WIN;
    }
}
