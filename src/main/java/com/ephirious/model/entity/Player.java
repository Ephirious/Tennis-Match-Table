package com.ephirious.model.entity;

import com.ephirious.model.value.PlayerName;
import lombok.Getter;
import xyz.block.uuidv7.UUIDv7;

import java.util.UUID;

@Getter
public class Player {
    private final UUID id;
    private final PlayerName name;

    public Player(PlayerName name) {
        id = UUIDv7.generate();
        this.name = name;
    }
}
