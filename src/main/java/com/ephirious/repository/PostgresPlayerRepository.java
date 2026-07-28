package com.ephirious.repository;

import com.ephirious.entity.PlayerJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.mapper.PlayerMapper;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresPlayerRepository extends AbstractJpaRepository<PlayerJpaEntity, UUID> implements PlayerRepository {
    private final Mapper<Player, PlayerJpaEntity> mapper;

    public PostgresPlayerRepository(EntityManagerFactory entityManagerFactory, PlayerMapper playerMapper) {
        super(entityManagerFactory, PlayerJpaEntity.class);
        this.mapper = playerMapper;
    }

    @Override
    public Optional<Player> findByName(PlayerName name) {
        return Optional.ofNullable(mapper.reverseMap(
                performReturning(entityManager ->
                        entityManager.createQuery(
                                        "select p from PlayerJpaEntity p where p.name = :name"
                                        , entityClass)
                                .setParameter("name", name.value())
                                .getSingleResultOrNull()
                )
        ));
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return Optional.ofNullable(mapper.reverseMap(
                performReturning(entityManager -> entityManager.find(entityClass, id)))
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

        return mapper.reverseMap(performReturning(
                        entityManager ->
                                (PlayerJpaEntity) entityManager.createNativeQuery(nativeSql, entityClass)
                                        .setParameter("id", player.id())
                                        .setParameter("name", player.name().value())
                                        .getSingleResult()
                )
        );
    }
}
