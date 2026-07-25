package com.objectstorage.backend.common.exception.base;

public class NotFoundException  extends ApiException {
    public NotFoundException(ErrorCode errorCode, String message) {

        super(errorCode, message);
    }
}