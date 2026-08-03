package com.ephirious.exception.database;

import com.ephirious.exception.ServerInternalException;

public class DatabaseException extends ServerInternalException {
    public DatabaseException(String serverMessage) {
        super(serverMessage);
    }
}
