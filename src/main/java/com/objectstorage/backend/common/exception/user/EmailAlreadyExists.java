package com.objectstorage.backend.common.exception.user;

import com.objectstorage.backend.common.exception.base.ConflictException;

public class EmailAlreadyExists extends ConflictException {
    public EmailAlreadyExists(String message) {
        super(message);
    }
}
