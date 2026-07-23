package com.ephirious.dto.request;

import com.ephirious.model.value.PlayerName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MatchCreateDto(
        @NotBlank(message = "The player name must not be null or empty")
        @Size(min = PlayerName.MIN_LENGTH, max = PlayerName.MAX_LENGTH, message = "The player name invalid by length")
        String firstPlayerName,

        @NotBlank(message = "The player name must not be null or empty")
        @Size(max = PlayerName.MAX_LENGTH, message = "The player name invalid by length")
        String secondPlayerName
) {}
