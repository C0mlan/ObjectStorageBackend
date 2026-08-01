package com.objectstorage.backend.common;

import com.objectstorage.backend.common.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class ResponseBuilder {
    // Centralizes creation of consistent API success and error responses.

    public static <T> ResponseEntity<ApiResponse<T>> success(
            HttpStatus status, String message, T data ){

        ApiResponse<T> ApiResponse = new ApiResponse<>(
                true,
                status.value(),
                null,
                message,
                data,
                null
        );

        return ResponseEntity.status(status).body(ApiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus status, ErrorCode errorCode, String message, List<Map<String, Object>> errors ){

        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .success(false)
                        .status(status.value())
                        .errorCode(errorCode)
                        .message(message)
                        .errors(errors)
                        .build());
    }
}
