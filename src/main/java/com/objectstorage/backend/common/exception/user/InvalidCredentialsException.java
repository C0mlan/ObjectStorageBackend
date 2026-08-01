package com.objectstorage.backend.common.exception.user;

import com.objectstorage.backend.common.exception.base.ErrorCode;
import com.objectstorage.backend.common.exception.base.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    private static final String DEFAULT_MESSAGE = "Invalid email or password.";

    public InvalidCredentialsException() { super(ErrorCode.INVALID_CREDENTIALS, DEFAULT_MESSAGE); }

    public InvalidCredentialsException(String message)

    { super(ErrorCode.INVALID_CREDENTIALS, message); }
}
