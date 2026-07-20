package com.ephirious.model.value.match;

public interface Game {
    boolean hasWinner();
    PlayerSide winner();
    Game pointTo(PlayerSide side);
}
