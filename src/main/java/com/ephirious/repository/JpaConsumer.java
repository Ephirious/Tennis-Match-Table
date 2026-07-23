package com.ephirious.repository;

import jakarta.persistence.EntityManager;

public interface JpaConsumer {
    void apply(EntityManager entityManager);
}
