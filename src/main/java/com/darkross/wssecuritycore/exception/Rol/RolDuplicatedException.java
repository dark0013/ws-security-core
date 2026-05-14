package com.darkross.wssecuritycore.exception.Rol;

import com.darkross.wssecuritycore.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RolDuplicatedException extends BusinessException {

    public RolDuplicatedException() {
        super(
                "ROL_ALREADY_EXISTS",
                "El rol ya está registrado",
                HttpStatus.CONFLICT
        );
    }

    public RolDuplicatedException(String message) {
        super("ROL_ALREADY_EXISTS", message, HttpStatus.CONFLICT);
    }
}
