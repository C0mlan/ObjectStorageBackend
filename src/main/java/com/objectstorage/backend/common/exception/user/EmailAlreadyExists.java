package com.objectstorage.backend.common.exception.user;

import com.objectstorage.backend.common.exception.base.ConflictException;
import com.objectstorage.backend.common.exception.base.ErrorCode;

public class EmailAlreadyExists extends ConflictException {
    private static final String DEFAULT_MESSAGE = "Email already exists.";

    public EmailAlreadyExists() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, DEFAULT_MESSAGE);
    }

    public EmailAlreadyExists(String message) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, message);
    }
}
