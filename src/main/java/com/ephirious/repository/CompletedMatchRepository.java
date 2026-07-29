package com.ephirious.repository;

import com.ephirious.model.aggregate.Match;

import java.util.UUID;

public interface CompletedMatchRepository {
    Match findById(UUID id);
    void add(Match match);
    void removeByID(UUID id);
    void remove(Match match);
    Match update(Match match);
}
