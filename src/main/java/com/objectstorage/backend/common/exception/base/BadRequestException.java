package com.objectstorage.backend.common.exception.base;

public class BadRequestException extends ApiException {
    public BadRequestException(ErrorCode errorCode, String message) {

        super(errorCode, message);
    }
}

