package com.ephirious.exception;

import org.springframework.http.HttpStatus;

public class ServerInternalException extends ApiException {
    public ServerInternalException(String serverMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", serverMessage);
    }
}
