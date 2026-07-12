package com.objectstorage.backend.common.exception.base;

public class NotFoundException  extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}