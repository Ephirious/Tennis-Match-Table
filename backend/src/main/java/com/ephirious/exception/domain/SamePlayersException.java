package com.ephirious.exception.domain;

import com.ephirious.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SamePlayersException extends ApiException {
    public SamePlayersException() {
        super(
                HttpStatus.BAD_REQUEST,
                "A player cannot play against himself in a match",
                "There was an attempt to create a match with two identical players"
        );
    }
}
