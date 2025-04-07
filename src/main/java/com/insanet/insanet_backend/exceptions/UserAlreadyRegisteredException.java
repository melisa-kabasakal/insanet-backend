package com.insanet.insanet_backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserAlreadyRegisteredException extends CustomException{
    public UserAlreadyRegisteredException(String message) {

        super(message, HttpStatus.CONFLICT);
    }
}
