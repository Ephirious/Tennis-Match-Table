package com.ephirious.repository;

import com.ephirious.model.entity.Player;
import com.ephirious.model.value.player.PlayerName;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {
    Player addIfAbsent(Player player);
    Optional<Player> findByName(PlayerName name);
    Optional<Player> findById(UUID id);
}
