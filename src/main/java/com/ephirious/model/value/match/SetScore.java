package com.ephirious.model.value.match;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetScore {
    private static final int SCORE_OF_DRAW = 5;
    private static final int SCORE_TO_WIN = 6;
    private static final int SCORE_TO_WIN_WITH_TIEBREAK = 7;

    private final int firstGamePoints;
    private final int secondGamePoints;
    private final Game currentGame;

    public SetScore() {
        firstGamePoints = secondGamePoints = 0;
        currentGame = new StandardGame();
    }

    public SetScore pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new IllegalStateException("Winner already exists");
        }

        Game newGame = currentGame.pointTo(side);
        if (newGame.hasWinner()) {
            int newFirst = newGame.winner() == PlayerSide.FIRST ? firstGamePoints + 1 : firstGamePoints;
            int newSecond = newGame.winner() == PlayerSide.SECOND ? secondGamePoints + 1 : secondGamePoints;

            if (newFirst == SCORE_TO_WIN && newSecond == SCORE_TO_WIN) {
                return new SetScore(newFirst, newSecond, new TieBreakGame());
            }
            return new SetScore(newFirst, newSecond, new StandardGame());
        }
        return new SetScore(firstGamePoints, secondGamePoints, newGame);
    }

    public boolean hasWinner() {
        return isGeneralWin() || isTiebreakWin();
    }

    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new IllegalStateException("The set's winner didn't define");
        }
        return firstGamePoints > secondGamePoints ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    private boolean isGeneralWin() {
        return (firstGamePoints == SCORE_TO_WIN && secondGamePoints < SCORE_OF_DRAW) ||
               (secondGamePoints == SCORE_TO_WIN && firstGamePoints < SCORE_OF_DRAW);
    }

    private boolean isTiebreakWin() {
        return firstGamePoints == SCORE_TO_WIN_WITH_TIEBREAK || secondGamePoints == SCORE_TO_WIN_WITH_TIEBREAK;
    }
}
