package com.ephirious.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String clientMessage;

    public ApiException(HttpStatus status, String userMessage, String serverMessage) {
        this.status = status;
        this.clientMessage = userMessage;
        super(serverMessage);
    }
}
