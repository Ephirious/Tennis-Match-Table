package com.ephirious.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;

public abstract class AbstractJpaRepository<T, ID> implements Repository<T, ID> {
    protected final EntityManagerFactory entityManagerFactory;
    protected final Class<T> entityClass;


    protected AbstractJpaRepository(EntityManagerFactory entityManagerFactory, Class<T> entityClass) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityClass = entityClass;
    }

    /*
    Методы:
        Optional<T> getById(ID id);
        List<T> getAllById(ID id);
     Не будут реализованы в абстрактном классе, так как появляются проблемы с транзакциями.
     Управление транзакцией нужно либо отдавать на слой сервиса, как это делается в Spring,
     либо забыть про Lazy-инициализацию объектов и писать Join Fetch запросы вручную через JPQL\HQL
     Это отдаётся на усмотрение пользователя, который будет наследовать данный класс
     */

    @Override
    public void add(T object) {
        perform(
                entityManager -> entityManager.persist(object)
        );
    }

    @Override
    public void removeById(ID id) {
        perform(
                entityManager -> {
                    T entity = entityManager.find(entityClass, id);
                    if (entity != null) {
                        entityManager.remove(entity);
                    }
                }
        );
    }

    @Override
    public void remove(T object) {
        perform(
                entityManager -> {
                    T entity = entityManager.merge(object);
                    entityManager.remove(entity);
                }
        );
    }

    @Override
    public void update(T object) {
        perform(
                entityManager -> entityManager.merge(object)
        );
    }

    protected void perform(JpaConsumer operations) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();

                operations.apply(entityManager);

                transaction.commit();

            } catch (PersistenceException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                mapException(exception);
            }
        }
    }

    protected void mapException(PersistenceException exception) {
        throw exception;
    }
}