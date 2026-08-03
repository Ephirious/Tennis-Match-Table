package com.ephirious.dto.response;

public record PlayerPointsMatchDto(
        String name,
        String points,
        int games,
        int sets,
        Integer tieBreakPoints
) {
}
