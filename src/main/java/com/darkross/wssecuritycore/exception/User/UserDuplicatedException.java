package com.darkross.wssecuritycore.exception.User;

import com.darkross.wssecuritycore.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserDuplicatedException extends BusinessException {

    public UserDuplicatedException(String message) {
        super("USER_ALREADY_EXISTS", message, HttpStatus.CONFLICT);
    }

    public UserDuplicatedException() {
        super("USER_ALREADY_EXISTS", "Los datos del usuario ya están registrados", HttpStatus.CONFLICT);
    }
}
