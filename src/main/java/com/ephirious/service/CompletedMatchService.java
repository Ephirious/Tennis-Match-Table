package com.ephirious.service;

import com.ephirious.model.aggregate.Match;
import com.ephirious.repository.CompletedMatchRepository;
import org.springframework.stereotype.Service;

@Service
public class CompletedMatchService {
    private final CompletedMatchRepository repository;

    public CompletedMatchService(CompletedMatchRepository postgresCompletedMatchRepository) {
        this.repository = postgresCompletedMatchRepository;
    }

    public void save(Match match) {
        repository.add(match);
    }
}
