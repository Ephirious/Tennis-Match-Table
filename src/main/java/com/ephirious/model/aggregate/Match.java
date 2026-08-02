package com.ephirious.model.aggregate;

import com.ephirious.exception.domain.ContractViolationException;
import com.ephirious.exception.domain.UnknowWhichPlayerAwardPointException;
import com.ephirious.model.value.score.PlayerSide;
import com.ephirious.model.value.score.match.AbstractMatchScore;
import com.ephirious.model.value.score.match.FiveSetMatchScore;
import com.ephirious.model.value.score.match.ThreeSetMatchScore;
import lombok.NonNull;
import xyz.block.uuidv7.UUIDv7;

import java.util.Objects;
import java.util.UUID;

public class Match {
    private final UUID id;
    private final UUID firstPlayerId;
    private final UUID secondPlayerId;
    private AbstractMatchScore score;

    public Match(@NonNull UUID firstPlayerId, @NonNull UUID secondPlayerId, @NonNull MatchType type) {
        ensureNotSameUuid(firstPlayerId, secondPlayerId);

        this.id = UUIDv7.generate();
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;

        this.score = switch (type) {
            case BEST_OF_THREE -> new ThreeSetMatchScore();
            case BEST_OF_FIVE -> new FiveSetMatchScore();
        };
    }

    public void pointTo(@NonNull UUID targetId) {
        if (matchEnded()) {
            throw new ContractViolationException(
                    "Can't increase the number of points for any player, because match is over"
            );
        }

        if (Objects.equals(firstPlayerId, targetId)) {
            score = score.pointTo(PlayerSide.FIRST);
        } else if (Objects.equals(secondPlayerId, targetId)) {
            score = score.pointTo(PlayerSide.SECOND);
        } else {
            throw new UnknowWhichPlayerAwardPointException(
                    "The specified player will not be awarded a point, as he does not belong to the match",
                    "Can't increase the number of points for any player, because match has two players: " +
                    "first - '%s', second - '%s', but target player has '%s' id"
                            .formatted(firstPlayerId, secondPlayerId, targetId)
            );
        }
    }

    public boolean matchEnded() {
        return score.hasWinner();
    }

    public UUID winner() {
        if (!matchEnded()) {
            throw new ContractViolationException("Not possible to get winner, because match is not over");
        }
        if (score.winner() == PlayerSide.FIRST) {
            return firstPlayerId;
        }
        return secondPlayerId;
    }

    public AbstractMatchScore score() {
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

    private void ensureNotSameUuid(UUID firstPlayerId, UUID secondPlayerId) {
        if (Objects.equals(firstPlayerId, secondPlayerId)) {
            throw new ContractViolationException("The two players have same id");
        }
    }
}
