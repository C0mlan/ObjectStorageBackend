package com.objectstorage.backend.common.validation;


import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordConstraintsValidatorTest {

    private PasswordConstraintsValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        validator = new PasswordConstraintsValidator();

        context = mock(ConstraintValidatorContext.class);
        violationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(violationBuilder);

        when(violationBuilder.addConstraintViolation())
                .thenReturn(context);
    }

    @Test
    void shouldReturnTrue_whenPasswordIsValid() {
        String password = "Password1!";

        boolean result = validator.isValid(password, context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void shouldReturnFalse_whenPasswordIsNull() {
        boolean result = validator.isValid(null, context);

        assertFalse(result);

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Password is required");
    }

    @Test
    void shouldReturnFalse_whenPasswordIsBlank() {
        boolean result = validator.isValid("   ", context);

        assertFalse(result);

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Password is required");
    }

    @Test
    void shouldReturnFalse_whenPasswordIsTooShort() {
        boolean result = validator.isValid("Ab1!", context);

        assertFalse(result);

        verify(context).buildConstraintViolationWithTemplate(
                "Password must be at least 8 characters long");
    }

    @Test
    void shouldReturnFalse_whenPasswordHasNoLowercase() {
        boolean result = validator.isValid("PASSWORD1!", context);

        assertFalse(result);

        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one lowercase letter");
    }

    @Test
    void shouldReturnFalse_whenPasswordHasNoUppercase() {
        boolean result = validator.isValid("password1!", context);

        assertFalse(result);

        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one uppercase letter");
    }

    @Test
    void shouldReturnFalse_whenPasswordHasNoDigit() {
        boolean result = validator.isValid("Password!", context);

        assertFalse(result);

        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one digit");
    }

    @Test
    void shouldReturnFalse_whenPasswordHasNoSpecialCharacter() {
        boolean result = validator.isValid("Password1", context);

        assertFalse(result);

        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one special character (@$!%*?&)");
    }

    @Test
    void shouldReturnMultipleViolations_whenPasswordFailsSeveralRules() {
        boolean result = validator.isValid("abc", context);

        assertFalse(result);

        verify(context).disableDefaultConstraintViolation();

        verify(context).buildConstraintViolationWithTemplate(
                "Password must be at least 8 characters long");
        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one uppercase letter");
        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one digit");
        verify(context).buildConstraintViolationWithTemplate(
                "Password must contain at least one special character (@$!%*?&)");
    }
}
