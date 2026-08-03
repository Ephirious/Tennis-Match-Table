package com.ephirious.service;

import com.ephirious.model.aggregate.Match;
import com.ephirious.repository.CompletedMatchRepository;
import com.ephirious.transaction.TransactionManager;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class CompletedMatchService {
    private final CompletedMatchRepository repository;
    private final TransactionManager transactionManager;

    public CompletedMatchService(
            CompletedMatchRepository postgresCompletedMatchRepository,
            TransactionManager transactionManager
    ) {
        this.repository = postgresCompletedMatchRepository;
        this.transactionManager = transactionManager;
    }

    public void save(@NonNull Match match) {
        transactionManager.executeInTransaction(() -> repository.add(match));
    }
}
