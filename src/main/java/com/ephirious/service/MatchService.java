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
import lombok.RequiredArgsConstructor;
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

        Match match = new Match(firstPlayer.id(), secondPlayer.id());

        ongoing.add(match);

        return new CreatedMatchDto(match.id());
    }

    public MatchStatusDto awardPoint(UUID matchId, PlayerName target) {
        Match match = getOngoingMatchOrThrow(matchId);
        Player first = getPlayerOrThrow(match.firstPlayerId());
        Player second = getPlayerOrThrow(match.secondPlayerId());

        ensureBothPlayersAreMatchPlayers(first.name(), second.name(), target, matchId);

        PlayerSide targetSide = Objects.equals(target, first.name()) ? PlayerSide.FIRST : PlayerSide.SECOND;
        match.pointTo(targetSide);

        Player maybeWinner = match.matchEnded()
                ? getPlayerOrThrow(match.winner())
                : null;

        return MatchStatusDto.fromMatch(match, first, second, maybeWinner);
    }

    public MatchStatusDto getMatch(UUID matchId) {
        Match match = getOngoingMatchOrThrow(matchId);
        Player first = getPlayerOrThrow(match.firstPlayerId());
        Player second = getPlayerOrThrow(match.secondPlayerId());
        Player maybeWinner = match.matchEnded()
                ? getPlayerOrThrow(match.winner())
                : null;

        return MatchStatusDto.fromMatch(match, first, second, maybeWinner);
    }

    private Player getPlayerOrThrow(UUID playerId) {
        return players.findById(playerId)
                .orElseThrow(
                        () -> new IllegalStateException("The player by '%s' id hasn't found".formatted(playerId))
                );
    }

    private Match getOngoingMatchOrThrow(UUID matchId) {
        return ongoing.findById(matchId)
                .orElseThrow(
                        () -> new IllegalStateException("The match with '%s' id isn't playing".formatted(matchId))
                );
    }

    private void ensureBothPlayersAreMatchPlayers(PlayerName first, PlayerName second, PlayerName target, UUID matchId) {
        if (!Objects.equals(target, first) && !Objects.equals(target, second)) {
            throw new IllegalStateException(
                    "The player with name '%s' isn't playing in match with '%s' id".formatted(target, matchId)
            );
        }
    }
}
