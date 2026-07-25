package com.ephirious.repository;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OngoingMatchRepository {
    private final ConcurrentMap<UUID, Match> matches;
    private final ConcurrentMap<Match, List<Player>> playersInConcreteGame;
    private final Set<Player> playersInGame;

    public OngoingMatchRepository() {
        this.matches = new ConcurrentHashMap<>();
        this.playersInConcreteGame = new ConcurrentHashMap<>();
        this.playersInGame = ConcurrentHashMap.newKeySet();
    }

    public Optional<Match> findById(@NonNull UUID id) {
        return Optional.ofNullable(
                matches.getOrDefault(id, null)
        );
    }

    public void add(@NonNull Match match, @NonNull Player first, @NonNull Player second) {
        putIfAbsentOrThrow(match);
        playersInConcreteGame.put(match, new ArrayList<>(List.of(first, second)));
        playersInGame.add(first);
        playersInGame.add(second);
    }

    public Optional<List<Player>> getPlayersByMatch(Match match) {
        return Optional.ofNullable(
                playersInConcreteGame.getOrDefault(match, null)
        );
    }

    public void remove(@NonNull Match match) {
        Match removed = matches.remove(match.id());

        if (removed == null) {
            throw new IllegalStateException("The match '%s' has already removed".formatted(match.id()));
        }

        List<Player> playersInMatch = playersInConcreteGame.remove(removed);

        if (playersInMatch != null && playersInMatch.size() != 2) {
            playersInGame.remove(playersInMatch.getFirst());
            playersInGame.remove(playersInMatch.getLast());
        }
    }

    public boolean alreadyPlaying(@NonNull Player player) {
        return playersInGame.contains(player);
    }


    private void putIfAbsentOrThrow(@NonNull Match match) {
        if (matches.putIfAbsent(match.id(), match) != null) {
            throw new IllegalStateException(
                    "The match with id '%s' has been playing".formatted(match.id())
            );
        }
    }
}
