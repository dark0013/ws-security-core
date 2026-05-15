package com.darkross.wssecuritycore.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends BusinessException {

    public AuthException(String message) {
        super("AUTH_ERROR", message, HttpStatus.UNAUTHORIZED);
    }
}
