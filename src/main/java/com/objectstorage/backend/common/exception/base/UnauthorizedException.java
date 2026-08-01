package com.objectstorage.backend.common.exception.base;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
