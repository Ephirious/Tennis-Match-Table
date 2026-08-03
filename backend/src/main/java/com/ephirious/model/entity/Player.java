package com.ephirious.model.entity;

import com.ephirious.model.value.player.PlayerName;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import xyz.block.uuidv7.UUIDv7;

import java.util.UUID;

@EqualsAndHashCode
@ToString
public class Player {
    private final UUID id;
    private final PlayerName name;

    public static Player reconstruct(@NonNull UUID id, @NonNull PlayerName name) {
        return new Player(id, name);
    }

    public Player(@NonNull PlayerName name) {
        this.id = UUIDv7.generate();
        this.name = name;
    }

    private Player(@NonNull UUID id, @NonNull PlayerName name) {
        this.id = id;
        this.name = name;
    }

    public UUID id() {
        return id;
    }

    public PlayerName name() {
        return name;
    }
}
