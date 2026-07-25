package com.objectstorage.backend.common.exception.base;

public class ForbiddenException extends ApiException{
    public ForbiddenException(ErrorCode errorCode, String message) {

        super(errorCode, message);
    }
}

