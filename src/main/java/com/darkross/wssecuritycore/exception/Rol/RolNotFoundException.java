package com.darkross.wssecuritycore.exception.Rol;

import com.darkross.wssecuritycore.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RolNotFoundException extends BusinessException {

    public RolNotFoundException() {
        super(
                "ROL_NOT_FOUND",
                "Rol no encontrado",
                HttpStatus.NOT_FOUND
        );
    }
}