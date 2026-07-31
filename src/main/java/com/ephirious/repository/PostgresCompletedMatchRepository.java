package com.ephirious.repository;

import com.ephirious.entity.MatchJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.model.aggregate.Match;
import com.ephirious.util.ThreadContext;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostgresCompletedMatchRepository implements CompletedMatchRepository {
    private final Mapper<Match, MatchJpaEntity> mapper;

    public PostgresCompletedMatchRepository(Mapper<Match, MatchJpaEntity> matchMapper) {
        this.mapper = matchMapper;
    }

    @Override
    public Match findById(@NonNull UUID id) {
        EntityManager entityManager = ThreadContext.get();
        return mapper.reverseMap(entityManager.find(MatchJpaEntity.class, id));
    }

    @Override
    public void add(@NonNull Match match) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = mapper.directMap(match);
        entityManager.persist(jpa);
    }

    @Override
    public void removeByID(@NonNull UUID id) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = entityManager.find(MatchJpaEntity.class, id);
        entityManager.remove(jpa);
    }

    @Override
    public void remove(@NonNull Match match) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = mapper.directMap(match);
        if (!entityManager.contains(jpa)) {
            entityManager.merge(jpa);
        }
        entityManager.remove(jpa);
    }

    @Override
    public Match update(@NonNull Match match) {
        EntityManager entityManager = ThreadContext.get();
        MatchJpaEntity jpa = mapper.directMap(match);
        return mapper.reverseMap(entityManager.merge(jpa));
    }
}
