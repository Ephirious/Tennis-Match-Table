package com.ephirious.repository;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OngoingMatchRepository {
    Optional<Match> findById(UUID id);
    void add(Match match, Player firstPlayer, Player secondPlayer);
    Optional<List<Player>> getPlayersByMatch(Match match);
    void remove(Match match);
    boolean alreadyPlaying(Player player);
}
