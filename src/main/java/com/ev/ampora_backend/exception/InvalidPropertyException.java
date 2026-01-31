package com.ev.ampora_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidPropertyException extends RuntimeException{
    public InvalidPropertyException(String message) {
        super(message);
    }
}