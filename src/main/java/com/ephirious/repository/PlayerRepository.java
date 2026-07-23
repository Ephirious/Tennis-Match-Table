package com.ephirious.repository;

import com.ephirious.entity.PlayerJpaEntity;
import com.ephirious.mapper.Mapper;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.PlayerName;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerRepository extends AbstractJpaRepository<PlayerJpaEntity, UUID> {
    Mapper<Player, PlayerJpaEntity> mapper;

    public PlayerRepository(EntityManagerFactory entityManagerFactory, Mapper<Player, PlayerJpaEntity> playerMapper) {
        this.mapper = playerMapper;
        super(entityManagerFactory, PlayerJpaEntity.class);
    }

    public Optional<Player> findById(UUID id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    mapper.reverseMap(entityManager.find(entityClass, id))
            );
        }
    }

    public Optional<Player> findByName(PlayerName name) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<PlayerJpaEntity> players = entityManager.createQuery(
                            "select p from PlayerJpaEntity p where p.name = :name",
                            entityClass
                    )
                    .setParameter("name", name.value())
                    .getResultList();
            if (!players.isEmpty()) {
                return Optional.of(mapper.reverseMap(players.getFirst()));
            }
        }

        return Optional.empty();
    }

    public Player add(Player player) {
        Optional<Player> optionalPlayer = findByName(player.name());
        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get();
        }

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();
                entityManager.persist(mapper.directMap(player));
                transaction.commit();
            } catch (PersistenceException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                mapException(exception);
            }
        }
        return player;
    }
}
