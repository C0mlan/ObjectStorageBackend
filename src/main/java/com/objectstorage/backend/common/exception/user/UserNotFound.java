package com.objectstorage.backend.common.exception.user;


import com.objectstorage.backend.common.exception.base.ErrorCode;
import com.objectstorage.backend.common.exception.base.NotFoundException;

public class UserNotFound extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "User not found.";

    public UserNotFound() {
        super(ErrorCode.USER_NOT_FOUND, DEFAULT_MESSAGE);
    }

    public UserNotFound(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}