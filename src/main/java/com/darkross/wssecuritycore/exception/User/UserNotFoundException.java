package com.darkross.wssecuritycore.exception.User;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    public UserNotFoundException() {
        super("Usuario no encontrado", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}

