package com.ephirious.exception.domain;

import com.ephirious.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnknowWhichPlayerAwardPointException extends ApiException {
    public UnknowWhichPlayerAwardPointException(String userMessage, String serverMessage) {
        super(HttpStatus.BAD_REQUEST, userMessage, serverMessage);
    }
}
