package com.ephirious.repository;

import com.ephirious.entity.MatchJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.model.aggregate.Match;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompletedMatchRepository extends AbstractJpaRepository<MatchJpaEntity, UUID> {
    Mapper<Match, MatchJpaEntity> mapper;

    public CompletedMatchRepository(EntityManagerFactory entityManagerFactory, Mapper<Match, MatchJpaEntity> matchMapper) {
        this.mapper = matchMapper;
        super(entityManagerFactory, MatchJpaEntity.class);
    }

    public void add(Match match) {
        super.add(mapper.directMap(match));
    }

}
