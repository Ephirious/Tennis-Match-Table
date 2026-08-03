package com.ephirious.exception.service;

import com.ephirious.exception.ServerInternalException;

public class MatchWinnerNotMatchPlayersException extends ServerInternalException {
    public MatchWinnerNotMatchPlayersException(String serverMessage) {
        super(serverMessage);
    }
}
