package com.ephirious.mapper;

import com.ephirious.entity.MatchJpaEntity;
import com.ephirious.entity.PlayerJpaEntity;
import com.ephirious.model.aggregate.Match;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper implements Mapper<Match, MatchJpaEntity> {
    @Override
    public MatchJpaEntity directMap(Match mapped) {
        PlayerJpaEntity first = new PlayerJpaEntity(mapped.firstPlayerId());
        PlayerJpaEntity second = new PlayerJpaEntity(mapped.secondPlayerId());
        PlayerJpaEntity winner = new PlayerJpaEntity(mapped.winner());

        return new MatchJpaEntity(
                mapped.id(),
                first,
                second,
                winner
        );
    }

    @Override
    public Match reverseMap(MatchJpaEntity mapped) {
        return null;
    }
}
