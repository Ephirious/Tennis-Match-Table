package com.ephirious.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String userMessage, String serverMessage) {
        super(HttpStatus.NOT_FOUND, userMessage, serverMessage);
    }
}
