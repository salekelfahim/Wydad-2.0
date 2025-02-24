package com.example.wydad.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPlayerException extends RuntimeException {
    public InvalidPlayerException(String message) {
        super(message);
    }
}
