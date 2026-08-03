package com.ephirious.model.value.score.game;

public enum TieBreakGameType {
    DEFAULT(7),
    BIG(10);

    private final int score;

    TieBreakGameType(int score) {
        this.score = score;
    }

    public int score() {
        return this.score;
    }
}
