package com.ephirious.exception.service;

import com.ephirious.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PlayersPlayingException extends ApiException {
    public PlayersPlayingException(String playerName) {
        super(
                HttpStatus.CONFLICT,
                "Player '%s' is playing now".formatted(playerName),
                "Exception was called when user wanted to create match with player who has been playing"
        );
    }
}
