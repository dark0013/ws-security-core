package com.darkross.wssecuritycore.exception.User;

import com.darkross.wssecuritycore.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message) {

        super("USER_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException() {
        super("USER_NOT_FOUND", "Usuario no encontrado", HttpStatus.NOT_FOUND);
    }
}
