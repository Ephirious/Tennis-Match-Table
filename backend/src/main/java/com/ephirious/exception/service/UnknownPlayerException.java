package com.ephirious.exception.service;

import com.ephirious.exception.NotFoundException;

public class UnknownPlayerException extends NotFoundException {
    public UnknownPlayerException(String userMessage, String serverMessage) {
        super(userMessage, serverMessage);
    }
}
