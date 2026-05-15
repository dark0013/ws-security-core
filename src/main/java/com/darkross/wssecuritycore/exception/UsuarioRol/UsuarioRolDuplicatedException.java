package com.darkross.wssecuritycore.exception.UsuarioRol;

import com.darkross.wssecuritycore.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UsuarioRolDuplicatedException extends BusinessException {

    public UsuarioRolDuplicatedException() {
        super(
                "USUARIO_ROL_ALREADY_EXISTS",
                "El usuario ya tiene asignado este rol",
                HttpStatus.CONFLICT
        );
    }
}

