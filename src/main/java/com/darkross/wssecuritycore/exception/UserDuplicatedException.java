package com.darkross.wssecuritycore.exception;

import org.springframework.http.HttpStatus;

public class UserDuplicatedException extends UserException {

    public UserDuplicatedException(String message) {
        super(message, HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }

    public UserDuplicatedException() {
        super("Los datos del usuario ya están registrados", HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }
}

