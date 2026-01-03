package com.costadelsur.api.exception;

public class ModelNotFoundException extends RuntimeException  {
    public ModelNotFoundException(String message) {
        super(message);
    }
}
