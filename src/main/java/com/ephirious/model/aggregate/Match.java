package com.ephirious.model.aggregate;

import com.ephirious.model.value.match.MatchScore;
import com.ephirious.model.value.match.PlayerSide;
import xyz.block.uuidv7.UUIDv7;

import java.util.UUID;

public class Match {
    private final UUID id;
    private final UUID firstPlayerId;
    private final UUID secondPlayerId;
    private MatchScore score;

    public Match(UUID firstPlayerId, UUID secondPlayerId) {
        this.id = UUIDv7.generate();
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.score = new MatchScore();
    }

    public void pointTo(PlayerSide side) {
        if (matchEnded()) {
            throw new IllegalStateException("The match has already ended");
        }
        score = score.pointTo(side);
    }

    public boolean matchEnded() {
        return score.hasWinner();
    }

    public UUID winner() {
        if (!matchEnded()) {
            throw new IllegalStateException("The match's winner didn't define");
        }
        if (score.winner() == PlayerSide.FIRST) {
            return firstPlayerId;
        }
        return secondPlayerId;
    }

    public MatchScore score() {
        return score;
    }

    public UUID id() {
        return id;
    }

    public UUID firstPlayerId() {
        return firstPlayerId;
    }

    public UUID secondPlayerId() {
        return secondPlayerId;
    }
}
