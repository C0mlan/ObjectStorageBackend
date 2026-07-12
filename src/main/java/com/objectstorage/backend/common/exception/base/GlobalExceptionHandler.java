package com.objectstorage.backend.common.exception.base;


import com.objectstorage.backend.common.ApiResponse;
import com.objectstorage.backend.common.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserConflictException(ConflictException ex) {
            return ResponseBuilder.error(
                    HttpStatus.CONFLICT,
                    ex.getMessage(),
                    null
            );
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        return ResponseBuilder.error(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null
        );
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex) {
        return ResponseBuilder.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseBuilder.error(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbiddenException(ForbiddenException ex) {
        return ResponseBuilder.error(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> Map.<String, Object>of(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        return ResponseBuilder.error(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
    }





        /**
         * Helper method to build consistent API error responses
         *
         * @param status  HTTP status code
         * @param message Error message
         * @param details Optional details (e.g., field validation errors)
         * @return ResponseEntity with ApiResponse body
         */
//        private ResponseEntity<ApiResponse> build (
//                HttpStatus status,
//                String message,
//                Map < String, String > details
//    ){
//            return ResponseEntity.status(status).body(
//                    new ApiResponse(
//                            Instant.now(),      // timestamp
//                            status.value(),     // HTTP status code
//                            status.getReasonPhrase(), // "Not Found", "Bad Request", etc.
//                            message,            // readable error message
//                            details             // optional map of additional info
//                    )
//            );
//        }
}




