package com.objectstorage.backend.common.util;

import jakarta.validation.ValidationException;

public final class EmailUtil {
    private EmailUtil(){}

    public static String normalize(String email){
        if(email ==null){
            throw new ValidationException("Email is required");

        }
        return email.trim().toLowerCase();
    }
}
