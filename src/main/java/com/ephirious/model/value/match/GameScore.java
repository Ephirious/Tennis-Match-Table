package com.ephirious.model.value.match;

public interface GameScore {
    boolean hasWinner();
    PlayerSide winner();
    GameScore pointTo(PlayerSide side);
    String firstPlayerPoints();
    String secondPlayerPoints();
}
