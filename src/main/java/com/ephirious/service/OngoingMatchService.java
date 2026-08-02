package com.ephirious.service;

import com.ephirious.exception.service.UnknownMatchException;
import com.ephirious.model.aggregate.Match;
import com.ephirious.model.aggregate.MatchType;
import com.ephirious.model.entity.Player;
import com.ephirious.repository.OngoingMatchRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OngoingMatchService {
    private final OngoingMatchRepository repository;

    public OngoingMatchService(OngoingMatchRepository ongoingMatchRepositoryImpl) {
        this.repository = ongoingMatchRepositoryImpl;
    }

    public Match startThreeSetMatch(@NonNull Player first, @NonNull Player second) {
        ensurePlayerNotPlayingNow(first);
        ensurePlayerNotPlayingNow(second);

        Match match = new Match(first.id(), second.id(), MatchType.BEST_OF_THREE);
        repository.add(match, first, second);
        return match;
    }

    public Match startFiveSetMatch(@NonNull Player first, @NonNull Player second) {
        ensurePlayerNotPlayingNow(first);
        ensurePlayerNotPlayingNow(second);

        Match match = new Match(first.id(), second.id(), MatchType.BEST_OF_FIVE);
        repository.add(match, first, second);
        return match;
    }

    public Match findMatchById(@NonNull UUID matchId) {
        return repository.findById(matchId)
                .orElseThrow(() -> new UnknownMatchException(
                        "The match can't be found",
                        "The match '%s' hasn't found".formatted(matchId))
                );
    }

    public void endMatch(@NonNull Match match) {
        repository.remove(match);
    }

    public List<Player> playersInMatch(@NonNull Match match) {
        return repository.getPlayersByMatch(match)
                .orElseThrow(() -> new UnknownMatchException(
                        "The player of the match can't be found",
                        "The player of the match '%s' hasn't found".formatted(match.id())
                ));
    }

    public Optional<Player> matchWinner(@NonNull Match match, @NonNull Player first, @NonNull Player second) {
        if (match.matchEnded()) {
            if (Objects.equals(first.id(), match.winner())) {
                return Optional.of(first);
            }
            if (Objects.equals(second.id(), match.winner())) {
                return Optional.of(first);
            }
            throw new IllegalStateException("The winner can't resolve");
        }
        return Optional.empty();
    }

    private void ensurePlayerNotPlayingNow(Player player) {
        if (repository.alreadyPlaying(player)) {
            throw new IllegalStateException("The player has been already playing");
        }
    }
}
