package com.ephirious.junit.model;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.value.match.PlayerSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.block.uuidv7.UUIDv7;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class MatchTest {
    private UUID first;
    private UUID second;
    private Match match;

    @BeforeEach
    public void initMatch() {
        first = UUIDv7.generate();
        second = UUIDv7.generate();
        match = new Match(first, second);
    }

    @Test
    void shouldThrowWhenTryToGetWinnerButItNotDefine() {
        assertThrows(IllegalStateException.class, () -> match.winner());
    }

    @Test
    void shouldGetFirstWinner() {
        int ballsToWin = 48;
        for (int i = 0; i < ballsToWin ; i++) {
            match.pointTo(PlayerSide.FIRST);
        }
        assertThat(match.winner()).isEqualTo(first);
    }

    @Test
    void shouldGetSecondWinner() {
        int ballsToWin = 48;
        for (int i = 0; i < ballsToWin ; i++) {
            match.pointTo(PlayerSide.SECOND);
        }
        assertThat(match.winner()).isEqualTo(second);
    }

}
