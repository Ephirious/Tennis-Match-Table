package com.ephirious.exception.domain;

import com.ephirious.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidPlayerNameException extends ApiException {

    public InvalidPlayerNameException(String clientMessage, String serverMessage) {
        super(HttpStatus.BAD_REQUEST, clientMessage, serverMessage);
    }

    public InvalidPlayerNameException(HttpStatus status, String userMessage, String serverMessage) {
        super(status, userMessage, serverMessage);
    }
}
