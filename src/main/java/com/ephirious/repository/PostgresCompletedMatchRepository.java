package com.ephirious.repository;

import com.ephirious.entity.MatchJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.model.aggregate.Match;
import com.ephirious.util.ThreadContext;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostgresCompletedMatchRepository implements CompletedMatchRepository {
    private final Mapper<Match, MatchJpaEntity> mapper;

    public PostgresCompletedMatchRepository(Mapper<Match, MatchJpaEntity> matchMapper) {
        this.mapper = matchMapper;
    }

    @Override
    public Match findById(UUID id) {
        EntityManager entityManager = ThreadContext.get();
        return mapper.reverseMap(entityManager.find(MatchJpaEntity.class, id));
    }

    @Override
    public void add(Match match) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = mapper.directMap(match);
        entityManager.persist(jpa);
    }

    @Override
    public void removeByID(UUID id) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = entityManager.find(MatchJpaEntity.class, id);
        entityManager.remove(jpa);
    }

    @Override
    public void remove(Match match) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = mapper.directMap(match);
        if (!entityManager.contains(jpa)) {
            entityManager.merge(jpa);
        }
        entityManager.remove(jpa);
    }

    @Override
    public Match update(Match match) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = mapper.directMap(match);
        return mapper.reverseMap(entityManager.merge(jpa));
    }
}
