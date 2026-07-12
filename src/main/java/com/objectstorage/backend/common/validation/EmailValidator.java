package com.objectstorage.backend.common.validation;


import com.objectstorage.backend.common.util.EmailUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        List<String> errors = new ArrayList<>();

        if (email == null || email.isBlank()) {
            errors.add("Email is required");
            return buildViolations(context, errors);
        }

        email = EmailUtil.normalize(email);

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email address is invalid");
        }

        return buildViolations(context, errors);
    }

    private boolean buildViolations(
            ConstraintValidatorContext context,
            List<String> errors) {

        if (errors.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error)
                    .addConstraintViolation();
        }

        return false;
    }
}