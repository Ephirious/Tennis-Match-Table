package com.ephirious.dto.request;

import com.ephirious.model.value.player.PlayerName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerNamePointDto(
        @NotBlank
        @Size(min = PlayerName.MIN_LENGTH, max = PlayerName.MAX_LENGTH, message = "The player name invalid by length")
        String name
) {
}
