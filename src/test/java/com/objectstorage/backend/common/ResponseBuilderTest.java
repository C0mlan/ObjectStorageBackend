package com.objectstorage.backend.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResponseBuilderTest {

    @Test
    void success_shouldReturnResponseEntityWithApiResponse() {
        ResponseEntity<ApiResponse<String>> response = ResponseBuilder.success(
                HttpStatus.OK,
                "Request successful",
                "payload"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<String> body = response.getBody();
        assertNotNull(body);

        assertEquals(200, body.getStatus());
        assertEquals("Request successful", body.getMessage());
        assertEquals("payload", body.getData());
        assertNull(body.getErrors());
    }

    @Test
    void error_shouldReturnResponseEntityWithErrors() {
        List<Map<String, Object>> errors = List.of(
                Map.of(
                        "field", "email",
                        "message", "Email is required"
                )
        );

        ResponseEntity<ApiResponse<String>> response = ResponseBuilder.error(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiResponse<String> body = response.getBody();
        assertNotNull(body);

        assertEquals(400, body.getStatus());
        assertEquals("Validation failed", body.getMessage());
        assertNull(body.getData());
        assertEquals(errors, body.getErrors());
    }
}