package com.myproject.auth.caffetteriaremastered.exceptionhandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidPasswordException extends RuntimeException{

    private final String errorCode;

    public InvalidPasswordException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}

