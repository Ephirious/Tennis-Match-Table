package com.ephirious.model.value.score;

public interface Score<T> {
    boolean hasWinner();
    PlayerSide winner();
    Score<?> pointTo(PlayerSide side);
    T firstPlayerScore();
    T secondPlayerScore();
}
