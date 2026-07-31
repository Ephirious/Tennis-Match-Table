package com.ephirious.util;

import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreadContext {
    private static final ThreadLocal<EntityManager> context = new ThreadLocal<>();

    public static void save(@NonNull EntityManager entityManager) {
        context.set(entityManager);
    }

    public static EntityManager get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
