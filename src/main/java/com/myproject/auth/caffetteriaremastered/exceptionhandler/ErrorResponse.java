package com.myproject.auth.caffetteriaremastered.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private final String errorMessage;
    private final String errorCode;

    public ErrorResponse(String errorMessage, HttpStatus errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = String.valueOf(errorCode);
    }

}