package com.ephirious.service;

import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchOrchestrator {
    private final PlayerService playerService;
    private final CompletedMatchService completedMatchService;
    private final OngoingMatchService ongoingMatchService;

    public CreatedMatchDto createMatch(PlayerName firstPlayerName, PlayerName secondPlayerName) {
        Player firstPlayer = playerService.getOrCreatePlayer(firstPlayerName);
        Player secondPlayer = playerService.getOrCreatePlayer(secondPlayerName);
        Match createdMatch = ongoingMatchService.startMatch(firstPlayer, secondPlayer);
        return new CreatedMatchDto(createdMatch.id());
    }

    public MatchStatusDto awardPoint(UUID matchUUID, PlayerName targetPlayerName) {
        Match match = ongoingMatchService.findMatchById(matchUUID);
        Player targetPlayer = playerService.findByName(targetPlayerName);
        match.pointTo(targetPlayer.id());

        List<Player> players = ongoingMatchService.playersInMatch(match);
        Player first = players.getFirst();
        Player second = players.getLast();

        resaveIfMatchEnded(match);

        return getPreparedMatchStatusDto(match, first, second);
    }

    public MatchStatusDto getPlayingMatch(UUID uuid) {
        Match match = ongoingMatchService.findMatchById(uuid);
        List<Player> players = ongoingMatchService.playersInMatch(match);

        return getPreparedMatchStatusDto(match, players.getFirst(), players.getLast());
    }

    private void resaveIfMatchEnded(Match match) {
        if (match.matchEnded()) {
            completedMatchService.save(match);
            ongoingMatchService.endMatch(match);
        }
    }

    private MatchStatusDto getPreparedMatchStatusDto(Match match, Player first, Player second) {
        return MatchStatusDto.fromMatch(
                match,
                first,
                second,
                ongoingMatchService.matchWinner(match, first, second)
        );
    }

}
