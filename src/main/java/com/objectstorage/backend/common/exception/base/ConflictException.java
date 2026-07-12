package com.objectstorage.backend.common.exception.base;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
