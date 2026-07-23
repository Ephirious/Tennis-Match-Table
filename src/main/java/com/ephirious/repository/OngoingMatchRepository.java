package com.ephirious.repository;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OngoingMatchRepository implements Repository<Match, UUID> {
    private final ConcurrentMap<UUID, Match> matches;
    private final Set<UUID> playersInGame;
    private final PlayerRepository players;

    public OngoingMatchRepository(PlayerRepository playerRepository) {
        this.matches = new ConcurrentHashMap<>();
        this.playersInGame = ConcurrentHashMap.newKeySet();
        this.players = playerRepository;
    }

    public Optional<Match> findById(UUID id) {
        return Optional.ofNullable(
                matches.getOrDefault(id, null)
        );
    }

    @Override
    public void add(Match match) {
        ensurePlayersNotPlayYet(match.firstPlayerId());
        ensurePlayersNotPlayYet(match.secondPlayerId());
        matches.put(match.id(), match);
        playersInGame.add(match.firstPlayerId());
        playersInGame.add(match.secondPlayerId());
    }

    @Override
    public void removeById(UUID id) {
        matches.remove(id);
    }

    @Override
    public void remove(Match object) {
        matches.remove(object.id());
    }

    @Override
    public void update(Match object) {
        matches.replace(object.id(), object);
    }

    private void ensurePlayersNotPlayYet(UUID playerId) {
        if (playersInGame.contains(playerId)) {
            throw new IllegalStateException(
                    "The player '%s' is playing now".formatted(
                            players.findById(playerId)
                                    .map(Player::name)
                                    .map(PlayerName::value)
                                    .orElseThrow(() -> new IllegalStateException(
                                            "The player with %s not exists".formatted(playerId)
                                    ))
                    )
            );
        }
    }
}
