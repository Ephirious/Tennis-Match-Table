package com.ephirious.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractJpaRepository<T, ID> {
    protected final EntityManagerFactory entityManagerFactory;
    protected final Class<T> entityClass;

    protected AbstractJpaRepository(EntityManagerFactory entityManagerFactory, Class<T> entityClass) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityClass = entityClass;
    }

    public Optional<T> getById(ID id) {
        return Optional.ofNullable(performReturning(
                entityManager -> entityManager.find(entityClass, id))
        );
    }

    public void add(T object) {
        perform(entityManager -> entityManager.persist(object));
    }

    public void removeById(ID id) {
        perform(entityManager -> {
            T object = entityManager.getReference(entityClass, id);
            entityManager.remove(object);
        });
    }

    public void remove(T object) {
        perform(entityManager -> {
            T newObject = entityManager.contains(object) ? object : entityManager.merge(object);
            entityManager.remove(newObject);
        });
    }

    public T update(T object) {
        return performReturning(
                entityManager -> entityManager.merge(object)
        );
    }

    protected <R> R performReturning(Function<EntityManager, R> operations) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();
                R result = operations.apply(entityManager);
                transaction.commit();

                return result;

            } catch (PersistenceException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw mapException(exception);
            }
        }
    }

    protected void perform(Consumer<EntityManager> operations) {
        performReturning((entityManager) -> {
            operations.accept(entityManager);
            return null;
        });
    }

    protected RuntimeException mapException(PersistenceException exception) {
        return exception;
    }
}