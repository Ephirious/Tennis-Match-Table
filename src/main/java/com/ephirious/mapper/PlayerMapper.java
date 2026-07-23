package com.ephirious.mapper;

import com.ephirious.entity.PlayerJpaEntity;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper implements Mapper<Player, PlayerJpaEntity> {
    @Override
    public PlayerJpaEntity directMap(Player mapped) {
        return new PlayerJpaEntity(mapped.id(), mapped.name().value());
    }

    @Override
    public Player reverseMap(PlayerJpaEntity mapped) {
        return Player.reconstruct(mapped.getId(), PlayerName.of(mapped.getName()));
    }
}
