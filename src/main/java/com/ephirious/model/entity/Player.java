package com.ephirious.model.entity;

import com.ephirious.model.value.PlayerName;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.block.uuidv7.UUIDv7;

import java.util.UUID;

@EqualsAndHashCode
@ToString
public class Player {
    private final UUID id;
    private PlayerName name;

    public Player(PlayerName name) {
        id = UUIDv7.generate();
        this.name = name;
    }

    public UUID id() {
        return id;
    }

    public PlayerName name() {
        return name;
    }
}
