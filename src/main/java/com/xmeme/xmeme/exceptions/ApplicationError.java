package com.xmeme.xmeme.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApplicationError extends Exception {
    
    private static final String DEFAULT_MSG = "Server side error. Please try again.";

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public ApplicationError() {
        super(DEFAULT_MSG);
    }

    public ApplicationError(String messageString) {
        super(messageString);
    }

    public ApplicationError(String messageString, HttpStatus httpStatus) {
        super(messageString);
        this.httpStatus = httpStatus;
    }
}
