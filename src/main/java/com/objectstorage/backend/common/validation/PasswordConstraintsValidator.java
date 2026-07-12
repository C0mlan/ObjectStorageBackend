package com.objectstorage.backend.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordConstraintsValidator
        implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern LOWER = Pattern.compile(".*[a-z].*");
    private static final Pattern UPPER = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL = Pattern.compile(".*[@$!%*?&].*");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        List<String> errors = new ArrayList<>();

        if (password == null || password.isBlank()) {
            errors.add("Password is required");
            return buildViolations(context, errors);
        }

        if (password.length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        if (!LOWER.matcher(password).matches()) {
            errors.add("Password must contain at least one lowercase letter");
        }

        if (!UPPER.matcher(password).matches()) {
            errors.add("Password must contain at least one uppercase letter");
        }

        if (!DIGIT.matcher(password).matches()) {
            errors.add("Password must contain at least one digit");
        }

        if (!SPECIAL.matcher(password).matches()) {
            errors.add("Password must contain at least one special character (@$!%*?&)");
        }

        return buildViolations(context, errors);
    }

    private boolean buildViolations(ConstraintValidatorContext context, List<String> errors) {

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