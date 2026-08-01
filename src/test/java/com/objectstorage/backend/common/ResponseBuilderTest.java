package com.objectstorage.backend.common;

import com.objectstorage.backend.common.exception.base.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseBuilderTest {

    // should build a successful response
    @Test
    void shouldBuildSuccessResponse() {

        Map<String, String> data = Map.of("message", "success");

        ResponseEntity<ApiResponse<Map<String, String>>> response =
                ResponseBuilder.success(
                        HttpStatus.OK,
                        "Request successful",
                        data
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<Map<String, String>> body = response.getBody();

        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals(200, body.getStatus());
        assertNull(body.getErrorCode());
        assertEquals("Request successful", body.getMessage());
        assertEquals(data, body.getData());
        assertNull(body.getErrors());
    }

    // should build an error response
    @Test
    void shouldBuildErrorResponse() {

        List<Map<String, Object>> errors = List.of(
                Map.of(
                        "field", "data",
                        "message", "Invalid email or password"
                )
        );

        ResponseEntity<ApiResponse<Void>> response =
                ResponseBuilder.error(
                        HttpStatus.UNAUTHORIZED,
                        ErrorCode.INVALID_CREDENTIALS,
                        "Invalid email or password",
                        errors
                );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ApiResponse<Void> body = response.getBody();

        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals(401, body.getStatus());
        assertEquals(ErrorCode.INVALID_CREDENTIALS, body.getErrorCode());
        assertEquals("Invalid email or password", body.getMessage());
        assertEquals(errors, body.getErrors());
        assertNull(body.getData());
    }
}

