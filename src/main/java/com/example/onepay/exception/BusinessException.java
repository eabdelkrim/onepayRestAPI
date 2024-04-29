package com.example.onepay.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

    public BusinessException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
