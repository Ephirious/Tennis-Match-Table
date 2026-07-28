package com.ephirious.repository;

import com.ephirious.entity.MatchJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.model.aggregate.Match;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PostgresCompletedMatchRepository extends AbstractJpaRepository<MatchJpaEntity, UUID> implements CompletedMatchRepository {
    private final Mapper<Match, MatchJpaEntity> mapper;

    public PostgresCompletedMatchRepository(EntityManagerFactory entityManagerFactory,
                                            Mapper<Match, MatchJpaEntity> matchMapper) {
        super(entityManagerFactory, MatchJpaEntity.class);
        this.mapper = matchMapper;
    }

    @Override
    public Match findById(UUID id) {
        return mapper.reverseMap(
                getById(id).orElseThrow(() -> new IllegalStateException("The match hasn't found"))
        );
    }

    @Override
    public void removeByID(Match match) {
        super.removeById(match.id());
    }

    @Override
    public void add(Match match) {
        super.add(mapper.directMap(match));
    }

    @Override
    public void remove(Match match) {
        super.remove(mapper.directMap(match));
    }

    @Override
    public Match update(Match match) {
        return mapper.reverseMap(
                super.update(mapper.directMap(match))
        );
    }
}
