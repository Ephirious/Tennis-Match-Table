package com.ephirious.service;

import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import com.ephirious.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository repository;

    public PlayerService(PlayerRepository postgresPlayerRepository) {
        this.repository = postgresPlayerRepository;
    }

    public Player getOrCreatePlayer(PlayerName name) {
        Player newPlayer = new Player(name);
        return repository.addIfAbsent(newPlayer);
    }

    public Player findByName(PlayerName name) {
        return repository.findByName(name)
                .orElseThrow(() -> new IllegalStateException("Unknown player name '%s'".formatted(name.value()))
                );
    }
}
