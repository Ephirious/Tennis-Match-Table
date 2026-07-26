package com.ephirious.service;

import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import com.ephirious.model.value.match.PlayerSide;
import com.ephirious.repository.CompletedMatchRepository;
import com.ephirious.repository.OngoingMatchRepository;
import com.ephirious.repository.PlayerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final OngoingMatchRepository ongoing;
    private final CompletedMatchRepository completed;
    private final PlayerRepository players;

    public CreatedMatchDto createMatch(PlayerName first, PlayerName second) {
        Player firstPlayer = players.add(new Player(first));
        Player secondPlayer = players.add(new Player(second));

        ensurePlayerNotYetPlaying(firstPlayer);
        ensurePlayerNotYetPlaying(secondPlayer);

        Match match = new Match(firstPlayer.id(), secondPlayer.id());

        ongoing.add(match, firstPlayer, secondPlayer);

        return new CreatedMatchDto(match.id());
    }

    public MatchStatusDto awardPoint(@NonNull UUID matchId, @NonNull PlayerName target) {
        Match match = getOngoingMatchOrThrow(matchId);
        Player first = getPlayerOrThrow(match, match.firstPlayerId());
        Player second = getPlayerOrThrow(match, match.secondPlayerId());

        match.pointTo(resolvePlayerIdByName(target, first, second));
        Player winner = winner(match);

        saveMatchIfEnded(match);

        return MatchStatusDto.fromMatch(match, first, second, winner);
    }

    public MatchStatusDto getMatch(@NonNull UUID matchId) {
        Match match = getOngoingMatchOrThrow(matchId);
        Player first = getPlayerOrThrow(match, match.firstPlayerId());
        Player second = getPlayerOrThrow(match, match.secondPlayerId());

        return MatchStatusDto.fromMatch(match, first, second, winner(match));
    }

    private void ensurePlayerNotYetPlaying(@NonNull Player player) {
        if (ongoing.alreadyPlaying(player)) {
            throw new IllegalStateException(
                    "Player '%s' has been playing".formatted(player.name().value())
            );
        }
    }

    private Player getPlayerOrThrow(@NonNull Match match, @NonNull UUID playerId) {
        return ongoing.getPlayersByMatch(match)
                .map(
                        players -> Objects.equals(playerId, match.firstPlayerId())
                                ? players.getFirst()
                                : players.getLast()
                )
                .orElseThrow(
                        () -> new IllegalStateException("Players of match '%s' hasn't found".formatted(match.id()))
                );
    }

    private Match getOngoingMatchOrThrow(@NonNull UUID matchId) {
        return ongoing.findById(matchId)
                .orElseThrow(
                        () -> new IllegalStateException("The match '%s' id hasn't found".formatted(matchId))
                );
    }

    private @Nullable Player winner(@NonNull Match match) {
        return match.matchEnded()
                ? getPlayerOrThrow(match, match.winner())
                : null;
    }

    private void saveMatchIfEnded(@NonNull Match match) {
        if (match.matchEnded()) {
            ongoing.remove(match);
            completed.add(match);
        }
    }

    private UUID resolvePlayerIdByName(@NonNull PlayerName target,
                                       @NonNull Player first,
                                       @NonNull Player second) {
        if (Objects.equals(target, first.name())) {
            return first.id();
        }
        if (Objects.equals(target, second.name())) {
            return second.id();
        }
        throw new IllegalStateException("Unknown player '%s'".formatted(target.value()));
    }
}
