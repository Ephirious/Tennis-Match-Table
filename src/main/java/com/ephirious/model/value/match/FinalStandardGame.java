package com.ephirious.model.value.match;

import lombok.RequiredArgsConstructor;

import static com.ephirious.model.value.match.FinalGameState.*;

@RequiredArgsConstructor
public class FinalStandardGame implements Game {
    private final FinalGameState state;

    @Override
    public boolean hasWinner() {
        return state == WIN_FIRST || state == WIN_SECOND;
    }

    @Override
    public PlayerSide winner() {
        return state == WIN_FIRST ? PlayerSide.FIRST : PlayerSide.SECOND;
    }

    @Override
    public Game pointTo(PlayerSide side) {
        if (hasWinner()) {
            throw new IllegalStateException("The game's winner already exists");
        }

        return switch (state) {
            case FinalGameState st when st == AD_FIRST && side == PlayerSide.FIRST -> new FinalStandardGame(WIN_FIRST);
            case FinalGameState st when st == AD_FIRST && side == PlayerSide.SECOND -> new FinalStandardGame(DEUCE);
            case FinalGameState st when st == AD_SECOND && side == PlayerSide.SECOND -> new FinalStandardGame(WIN_SECOND);
            case FinalGameState st when st == AD_SECOND && side == PlayerSide.FIRST -> new FinalStandardGame(DEUCE);
            case FinalGameState st when st == DEUCE && side == PlayerSide.FIRST -> new FinalStandardGame(AD_FIRST);
            case FinalGameState st when st == DEUCE && side == PlayerSide.SECOND -> new FinalStandardGame(AD_SECOND);
            default -> throw new IllegalStateException();
        };
    }
}
