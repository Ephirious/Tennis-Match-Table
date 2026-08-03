package com.ephirious.model.value.score.game;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.model.value.score.PlayerSide;
import lombok.RequiredArgsConstructor;

import static com.ephirious.model.value.score.game.FinalGameState.*;

@RequiredArgsConstructor
public class FinalStandardGameScore extends AbstractGameScore<String> {
    private final FinalGameState state;

    @Override
    public boolean hasWinner() {
        return state == WIN_FIRST || state == WIN_SECOND;
    }

    @Override
    public PlayerSide winner() {
        if (!hasWinner()) {
            throw new ContractViolationException("Can't get game's winner, because the game has not ended");
        }
        return state == WIN_FIRST ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    @Override
    public FinalStandardGameScore pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new ContractViolationException("Can't increase point in the game, because the game is over");
        }

        return switch (state) {
            case FinalGameState st when st == AD_FIRST && side == PlayerSide.FIRST -> new FinalStandardGameScore(WIN_FIRST);
            case FinalGameState st when st == AD_FIRST && side == PlayerSide.SECOND -> new FinalStandardGameScore(DEUCE);
            case FinalGameState st when st == AD_SECOND && side == PlayerSide.SECOND -> new FinalStandardGameScore(WIN_SECOND);
            case FinalGameState st when st == AD_SECOND && side == PlayerSide.FIRST -> new FinalStandardGameScore(DEUCE);
            case FinalGameState st when st == DEUCE && side == PlayerSide.FIRST -> new FinalStandardGameScore(AD_FIRST);
            case FinalGameState st when st == DEUCE && side == PlayerSide.SECOND -> new FinalStandardGameScore(AD_SECOND);
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public String firstPlayerScore() {
        return switch (state) {
            case AD_FIRST -> "AD";
            case AD_SECOND, DEUCE -> "40";
            case WIN_FIRST, WIN_SECOND -> "0";
        };
    }

    @Override
    public String secondPlayerScore() {
        return switch (state) {
            case AD_SECOND -> "AD";
            case AD_FIRST, DEUCE -> "40";
            case WIN_FIRST, WIN_SECOND -> "0";
        };
    }

    public boolean isDeuce() {
        return state == DEUCE;
    }

    public boolean isAdvantageFirst() {
        return state == AD_FIRST;
    }

    public boolean isAdvantageSecond() {
        return state == AD_SECOND;
    }
}
