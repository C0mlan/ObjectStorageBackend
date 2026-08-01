package com.objectstorage.backend.common.exception.base;

public class ConflictException extends ApiException {
    public ConflictException(ErrorCode errorCode, String message) {

        super(errorCode, message);
    }
}
