package com.insanet.insanet_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
