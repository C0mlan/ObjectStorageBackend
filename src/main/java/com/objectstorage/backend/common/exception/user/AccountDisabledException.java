package com.objectstorage.backend.common.exception.user;

import com.objectstorage.backend.common.exception.base.ErrorCode;
import com.objectstorage.backend.common.exception.base.UnauthorizedException;

public class AccountDisabledException extends UnauthorizedException {

    private static final String DEFAULT_MESSAGE = "Your account has been disabled.";

    public AccountDisabledException() {
        super(ErrorCode.ACCOUNT_DISABLED, DEFAULT_MESSAGE);
    }

    public AccountDisabledException(String message) {
        super(ErrorCode.ACCOUNT_DISABLED, message);
    }
}