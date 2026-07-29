package com.ephirious.repository;

import com.ephirious.entity.PlayerJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.mapper.PlayerMapper;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import com.ephirious.util.ThreadContext;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresPlayerRepository implements PlayerRepository {
    private final Mapper<Player, PlayerJpaEntity> mapper;

    public PostgresPlayerRepository(PlayerMapper playerMapper) {
        this.mapper = playerMapper;
    }

    @Override
    public Optional<Player> findByName(PlayerName name) {
        EntityManager entityManager = ThreadContext.get();
        String jpql = "SELECT p FROM PlayerJpaEntity p where p.name = :name";
        PlayerJpaEntity player = entityManager.createQuery(jpql, PlayerJpaEntity.class)
                .setParameter("name", name.value())
                .getSingleResult();
        return Optional.ofNullable(mapper.reverseMap(player));
    }

    @Override
    public Optional<Player> findById(UUID id) {
        EntityManager entityManager = ThreadContext.get();
        return Optional.ofNullable(
                mapper.reverseMap(entityManager.find(PlayerJpaEntity.class, id))
        );
    }

    @Override
    public Player addIfAbsent(Player player) {
        String nativeSql = """
                WITH ins AS (
                    INSERT INTO players (id, name)
                    VALUES (:id ,:name)
                    ON CONFLICT (name) DO NOTHING
                    RETURNING *
                )
                SELECT * FROM ins
                UNION ALL
                SELECT * FROM players WHERE name = :name
                LIMIT 1;
                """;

        EntityManager entityManager = ThreadContext.get();
        PlayerJpaEntity jpaPlayer = (PlayerJpaEntity) entityManager.createNativeQuery(nativeSql, PlayerJpaEntity.class)
                .setParameter("id", player.id())
                .setParameter("name", player.name().value())
                .getSingleResult();

        return mapper.reverseMap(jpaPlayer);
    }
}
