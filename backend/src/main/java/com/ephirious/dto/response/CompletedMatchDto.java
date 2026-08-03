package com.ephirious.dto.response;

import java.util.Objects;

public record CompletedMatchDto(
        String firstPlayerName,
        String secondPlayerName,
        String winnerName
) {

    public CompletedMatchDto {
        Objects.requireNonNull(firstPlayerName, "The player name must not be null");
        Objects.requireNonNull(secondPlayerName, "The player name must not be null");
        Objects.requireNonNull(winnerName, "The player name must not be null");
        ensurePlayersNotSame(firstPlayerName, secondPlayerName);
        ensureWinnerAmongPlayers(firstPlayerName, secondPlayerName, winnerName);
    }

    private void ensurePlayersNotSame(String first, String second) {
        if (Objects.equals(first, second)) {
            throw new IllegalStateException("The players are the same");
        }
    }

    private void ensureWinnerAmongPlayers(String first, String second, String winner) {
        if (!Objects.equals(first, winner) && !Objects.equals(second, winner)) {
            throw new IllegalStateException("The winner is not among players");
        }
    }
}
