package com.example.onepay.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public TransactionNotFoundException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
