package com.ephirious.dto.request;

import jakarta.validation.constraints.Min;

public record MatchesFilterDto(
        @Min(value = 1, message = "The page value must not be negative or equal zero") Integer page,
        String playerName
) {
    public MatchesFilterDto {
        if (page == null) {
            page = 1;
        }
    }

    public boolean hasPlayerName() {
        return playerName != null;
    }
}
