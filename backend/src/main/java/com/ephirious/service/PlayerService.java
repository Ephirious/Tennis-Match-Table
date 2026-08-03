package com.ephirious.service;

import com.ephirious.exception.service.UnknownPlayerException;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.player.PlayerName;
import com.ephirious.repository.PlayerRepository;
import com.ephirious.transaction.TransactionManager;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository repository;
    private final TransactionManager transactionManager;

    public PlayerService(PlayerRepository postgresPlayerRepository, TransactionManager transactionManager) {
        this.repository = postgresPlayerRepository;
        this.transactionManager = transactionManager;
    }

    public Player getOrCreatePlayer(PlayerName name) {
        Player newPlayer = new Player(name);
        return transactionManager.executeInTransactionReturned(() -> repository.addIfAbsent(newPlayer));
    }

    public Player findByName(PlayerName name) {
        return transactionManager.executeInTransactionReturned(() -> repository.findByName(name))
                .orElseThrow(
                        () -> new UnknownPlayerException(
                                "The player '%s' can't be found".formatted(name.value()),
                                "Unknown player name '%s'".formatted(name.value())
                        )
                );
    }
}
