package com.example.shopping.exception;

public class QuantityNotEnoughException extends RuntimeException {
    public QuantityNotEnoughException(String message) {
        super(message);
    }
}
