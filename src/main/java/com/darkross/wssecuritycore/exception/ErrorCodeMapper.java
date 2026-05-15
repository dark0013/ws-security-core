package com.darkross.wssecuritycore.exception;

import org.springframework.http.HttpStatus;

public class ErrorCodeMapper {

    public static HttpStatus getHttpStatusForErrorCode(String errorCode) {
        return switch (errorCode) {
            // User errors
            case "USER_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "USER_ALREADY_EXISTS" -> HttpStatus.CONFLICT;

            // Role errors
            case "ROL_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "ROL_ALREADY_EXISTS" -> HttpStatus.CONFLICT;

            // Usuario Rol errors
            case "USUARIO_ROL_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "USUARIO_ROL_ALREADY_EXISTS" -> HttpStatus.CONFLICT;

            // Product errors
            case "PRODUCT_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "PRODUCT_ALREADY_EXISTS" -> HttpStatus.CONFLICT;

            // Validation errors
            case "VALIDATION_ERROR" -> HttpStatus.BAD_REQUEST;

            // Default
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    // Error code constants for better maintainability
    public static class User {
        public static final String NOT_FOUND = "USER_NOT_FOUND";
        public static final String ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    }

    public static class Rol {
        public static final String NOT_FOUND = "ROL_NOT_FOUND";
        public static final String ALREADY_EXISTS = "ROL_ALREADY_EXISTS";
    }

    public static class UsuarioRol {
        public static final String NOT_FOUND = "USUARIO_ROL_NOT_FOUND";
        public static final String ALREADY_EXISTS = "USUARIO_ROL_ALREADY_EXISTS";
    }

    public static class Product {
        public static final String NOT_FOUND = "PRODUCT_NOT_FOUND";
        public static final String ALREADY_EXISTS = "PRODUCT_ALREADY_EXISTS";
    }

    public static class Validation {
        public static final String ERROR = "VALIDATION_ERROR";
    }

    public static class General {
        public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    }
}
