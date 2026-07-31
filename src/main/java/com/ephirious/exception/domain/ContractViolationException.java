package com.ephirious.exception.domain;

import com.ephirious.exception.ServerInternalException;

public class ContractViolationException extends ServerInternalException {
    public ContractViolationException(String serverMessage) {
        super(serverMessage);
    }
}
