package com.darkross.wssecuritycore.exception.User;


import com.darkross.wssecuritycore.exception.BusinessException;

public class UserException extends BusinessException {

    public UserException(String errorCode, String message) {
        super(errorCode, message, null);
    }

    public UserException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, null);
        initCause(cause);
    }
}
