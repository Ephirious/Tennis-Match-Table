package com.ephirious.exception.repository;

import com.ephirious.exception.ServerInternalException;

public class OngoingMatchAlreadyRemovedException extends ServerInternalException {
    public OngoingMatchAlreadyRemovedException(String serverMessage) {
        super(serverMessage);
    }
}
