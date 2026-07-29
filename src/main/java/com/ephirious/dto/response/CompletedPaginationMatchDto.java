package com.ephirious.dto.response;

import java.util.List;
import java.util.Objects;

public record CompletedPaginationMatchDto(
    List<CompletedMatchDto> matches,
    long currentPage,
    long totalPages
) {
    private static final long MIN_AVAILABLE_PAGE = 1;
    private static final long MIN_AVAILABLE_NUMBER_PAGES = 0;

    public CompletedPaginationMatchDto {
        Objects.requireNonNull(matches, "The list of matches must not be null");
        ensureMoreThan(currentPage, MIN_AVAILABLE_PAGE, "The page number must not be negative or equal zero");
        ensureMoreThan(totalPages, MIN_AVAILABLE_NUMBER_PAGES, "The number of pages must not be negative or equal zero");
    }

    private void ensureMoreThan(long checked, long bound, String message) {
        Objects.requireNonNull(message, "The error message must not be null");
        if (checked < bound) {
            throw new IllegalArgumentException(message);
        }
    }

}
