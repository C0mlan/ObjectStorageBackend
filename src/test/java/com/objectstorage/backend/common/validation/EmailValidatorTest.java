package com.objectstorage.backend.common.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailValidatorTest {

    private EmailValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();

        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(builder);

        when(builder.addConstraintViolation())
                .thenReturn(context);
    }

    @Test
    void shouldReturnFalseWhenEmailIsNull() {
        boolean result = validator.isValid(null, context);

        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Email is required");
    }

    @Test
    void shouldReturnFalseWhenEmailIsBlank() {
        boolean result = validator.isValid("   ", context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Email is required");
    }

    @Test
    void shouldReturnTrueForValidEmail() {
        boolean result = validator.isValid("test@example.com", context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void shouldReturnFalseForInvalidEmail() {
        boolean result = validator.isValid("test@", context);

        assertFalse(result);
        verify(context).buildConstraintViolationWithTemplate("Email address is invalid");
    }
}
