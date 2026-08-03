package com.ephirious.service;

import com.ephirious.dto.response.CompletedPaginationMatchDto;
import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.player.PlayerName;
import com.ephirious.transaction.TransactionManager;
import lombok.NonNull;
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
    private final MatchQueryService matchQueryService;
    private final TransactionManager transactionManager;

    public CreatedMatchDto createBestOfThreeMatch(@NonNull PlayerName firstPlayerName, @NonNull PlayerName secondPlayerName) {
        return transactionManager.executeInTransactionReturned(
                () -> createThreeSetMatchInternal(firstPlayerName, secondPlayerName)
        );
    }

    public CreatedMatchDto createBestOfFiveMatch(@NonNull PlayerName firstPlayerName, @NonNull PlayerName secondPlayerName) {
        return transactionManager.executeInTransactionReturned(
                () -> createFiveSetMatchInternal(firstPlayerName, secondPlayerName)
        );
    }

    public MatchStatusDto awardPoint(@NonNull UUID matchUUID, @NonNull PlayerName targetPlayerName) {
        return transactionManager.executeInTransactionReturned(
                () -> awardPointInternal(matchUUID, targetPlayerName)
        );
    }

    public MatchStatusDto getPlayingMatch(@NonNull UUID uuid) {
        Match match = ongoingMatchService.findMatchById(uuid);
        List<Player> players = ongoingMatchService.playersInMatch(match);
        return getPreparedMatchStatusDto(match, players.getFirst(), players.getLast());
    }

    public CompletedPaginationMatchDto getCompletedMatches(int page) {
        return matchQueryService.getCompletedMatches(page);
    }

    public CompletedPaginationMatchDto getCompletedMatchesByName(int page, @NonNull PlayerName name) {
        return transactionManager.executeInTransactionReturned(
                () -> matchQueryService.getCompletedMatchesByPlayer(page, name)
        );
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
                ongoingMatchService.matchWinner(match, first, second).orElse(null)
        );
    }

    private CreatedMatchDto createThreeSetMatchInternal(PlayerName firstPlayerName, PlayerName secondPlayerName) {
        Player firstPlayer = playerService.getOrCreatePlayer(firstPlayerName);
        Player secondPlayer = playerService.getOrCreatePlayer(secondPlayerName);
        Match createdMatch = ongoingMatchService.startThreeSetMatch(firstPlayer, secondPlayer);
        return new CreatedMatchDto(createdMatch.id());
    }

    private CreatedMatchDto createFiveSetMatchInternal(@NonNull PlayerName firstPlayerName, @NonNull PlayerName secondPlayerName) {
        Player firstPlayer = playerService.getOrCreatePlayer(firstPlayerName);
        Player secondPlayer = playerService.getOrCreatePlayer(secondPlayerName);
        Match createdMatch = ongoingMatchService.startFiveSetMatch(firstPlayer, secondPlayer);
        return new CreatedMatchDto(createdMatch.id());
    }

    private MatchStatusDto awardPointInternal(UUID matchUUID, PlayerName targetPlayerName) {
        Match match = ongoingMatchService.findMatchById(matchUUID);
        Player targetPlayer = playerService.findByName(targetPlayerName);
        match.pointTo(targetPlayer.id());

        List<Player> players = ongoingMatchService.playersInMatch(match);
        Player first = players.getFirst();
        Player second = players.getLast();

        resaveIfMatchEnded(match);

        return getPreparedMatchStatusDto(match, first, second);
    }
}
