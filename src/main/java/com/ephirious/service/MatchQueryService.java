package com.ephirious.service;

import com.ephirious.dto.response.CompletedMatchDto;
import com.ephirious.dto.response.CompletedPaginationMatchDto;
import com.ephirious.model.value.PlayerName;
import com.ephirious.transaction.TransactionManager;
import com.ephirious.util.ThreadContext;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatchQueryService {
    private record MatchTransactionResult(List<CompletedMatchDto> matches, long countMatches) { }

    private static final int PAGE_SIZE = 5;

    private static final String MATCHES_JPQL = """
            SELECT NEW com.ephirious.dto.response.CompletedMatchDto(p1.name, p2.name, w.name)
            FROM MatchJpaEntity m
            JOIN m.firstPlayer p1
            JOIN m.secondPlayer p2
            JOIN m.winner w
            """;

    private static final String NUMBER_MATCHES_JPQL = "SELECT COUNT(m) FROM MatchJpaEntity m";

    private static final String MATCHES_BY_PLAYER_JPQL = MATCHES_JPQL +
                                                         "WHERE p1.name = :name OR p2.name = :name";

    private static final String NUMBER_MATCHES_BY_PLAYER_JPQL = """
            SELECT COUNT(m) FROM MatchJpaEntity m
            JOIN m.firstPlayer p1
            JOIN m.secondPlayer p2
            WHERE p1.name = :name OR p2.name = :name
            """;

    private final TransactionManager transactionManager;

    public CompletedPaginationMatchDto getCompletedMatches(int page) {
        MatchTransactionResult result = transactionManager.executeInTransactionReturned(
                () -> getCompletedMatchesInternal(page)
        );
        return new CompletedPaginationMatchDto(
                result.matches,
                page,
                Math.ceilDiv(result.countMatches, PAGE_SIZE)
        );
    }

    public CompletedPaginationMatchDto getCompletedMatchesByPlayer(int page, @NonNull PlayerName name) {
        MatchTransactionResult result = transactionManager.executeInTransactionReturned(
                () -> getCompletedMatchesByPlayerInternal(page, name)
        );
        return new CompletedPaginationMatchDto(
                result.matches,
                page,
                Math.ceilDiv(result.countMatches, PAGE_SIZE)
        );
    }

    private MatchTransactionResult getCompletedMatchesInternal(int page) {
        EntityManager entityManager = ThreadContext.get();
        int offset = (page - 1) * PAGE_SIZE;
        List<CompletedMatchDto> matches = entityManager.createQuery(MATCHES_JPQL, CompletedMatchDto.class)
                .setFirstResult(offset)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
        Long numberMatches = entityManager.createQuery(NUMBER_MATCHES_JPQL, Long.class)
                .getSingleResult();
        return new MatchTransactionResult(matches, numberMatches);
    }

    private MatchTransactionResult getCompletedMatchesByPlayerInternal(int page, PlayerName name) {
        EntityManager entityManager = ThreadContext.get();
        int offset = (page - 1) * PAGE_SIZE;
        List<CompletedMatchDto> matches = entityManager.createQuery(MATCHES_BY_PLAYER_JPQL, CompletedMatchDto.class)
                .setFirstResult(offset)
                .setMaxResults(PAGE_SIZE)
                .setParameter("name", name.value())
                .getResultList();
        Long numberMatches = entityManager.createQuery(NUMBER_MATCHES_BY_PLAYER_JPQL, Long.class)
                .setParameter("name", name.value())
                .getSingleResult();
        return new MatchTransactionResult(matches, numberMatches);
    }
}
