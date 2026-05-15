package com.darkross.wssecuritycore.exception.UsuarioRol;

import com.darkross.wssecuritycore.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UsuarioRolNotFoundException extends BusinessException {

    public UsuarioRolNotFoundException() {
        super(
                "USUARIO_ROL_NOT_FOUND",
                "Asignación de usuario-rol no encontrada",
                HttpStatus.NOT_FOUND
        );
    }
}

