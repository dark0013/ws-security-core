package com.darkross.wssecuritycore.exception;

import com.darkross.wssecuritycore.dto.ErrorResponseDto;
import com.darkross.wssecuritycore.exception.User.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleUserException(UserException ex, WebRequest request) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(ex.getHttpStatus().value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorResponseDto.FieldErrorDto> fieldErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.add(
                        ErrorResponseDto.FieldErrorDto.builder()
                                .field(error.getField())
                                .message(error.getDefaultMessage())
                                .rejectedValue(error.getRejectedValue())
                                .build()
                )
        );

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .errorCode("VALIDATION_ERROR")
                .message("Error en la validación de datos")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .fieldErrors(fieldErrors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("Error interno del servidor")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

