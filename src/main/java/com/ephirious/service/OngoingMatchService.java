package com.ephirious.service;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.repository.OngoingMatchRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OngoingMatchService {
    private final OngoingMatchRepository repository;

    public OngoingMatchService(OngoingMatchRepository ongoingMatchRepositoryImpl) {
        this.repository = ongoingMatchRepositoryImpl;
    }

    public Match startMatch(Player first, Player second) {
        ensurePlayerNotPlayingNow(first);
        ensurePlayerNotPlayingNow(second);
        Match match = new Match(first.id(), second.id());
        repository.add(match, first, second);
        return match;
    }

    public Match findMatchById(UUID matchId) {
        return repository.findById(matchId)
                .orElseThrow(() -> new IllegalStateException("The match doesn't exist"));
    }


    public void endMatch(Match match) {
        repository.remove(match);
    }

    public List<Player> playersInMatch(Match match) {
        return repository.getPlayersByMatch(match)
                .orElseThrow(() -> new IllegalStateException("Unknown match"));
    }

    public @Nullable Player matchWinner(Match match, Player first, Player second) {
        if (match.matchEnded()) {
            if (Objects.equals(first.id(), match.winner())) {
                return first;
            }
            if (Objects.equals(second.id(), match.winner())) {
                return second;
            }
            throw new IllegalStateException("The winner can't resolve");
        }
        return null;
    }

    private void ensurePlayerNotPlayingNow(Player player) {
        if (repository.alreadyPlaying(player)) {
            throw new IllegalStateException("The player has been already playing");
        }
    }
}
