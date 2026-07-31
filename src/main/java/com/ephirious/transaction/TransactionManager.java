package com.ephirious.transaction;

import com.ephirious.util.ThreadContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionManager {
    private final EntityManagerFactory entityManagerFactory;

    public <R> R executeInTransactionReturned(@NonNull Supplier<R> operations) {
        EntityManager alreadyExisted = ThreadContext.get();
        if (alreadyExisted != null && alreadyExisted.getTransaction().isActive()) {
            return operations.get();
        }

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                ThreadContext.save(entityManager);
                transaction.begin();
                R result = operations.get();
                transaction.commit();
                return result;
            } catch (PersistenceException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            } finally {
                ThreadContext.clear();
            }
        }
    }

    public void executeInTransaction(@NonNull Runnable operations) {
        executeInTransactionReturned(
                () -> {
                    operations.run();
                    return null;
                }
        );
    }
}
