package com.ephirious.exception.service;

import com.ephirious.exception.NotFoundException;

public class UnknownMatchException extends NotFoundException {
    public UnknownMatchException(String userMessage, String serverMessage) {
        super(userMessage, serverMessage);
    }
}
