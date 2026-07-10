package com.objectstorage.backend.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(
            HttpStatus status, String message, T data ){

        ApiResponse<T> ApiResponse = new ApiResponse<>(
                status.value(),
                message,
                data,
                null
        );

        return ResponseEntity.status(status).body(ApiResponse);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus status, String message, List<Map<String, Object>> errors ){

        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .status(status.value())
                        .message(message)
                        .errors(errors)
                        .build());
    }
}
