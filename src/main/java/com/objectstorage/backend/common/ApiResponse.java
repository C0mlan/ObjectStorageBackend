package com.objectstorage.backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.objectstorage.backend.common.exception.base.ErrorCode;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    // Standard API response wrapper used for all successful and error responses.
    private boolean success;
    private int status;

    private ErrorCode errorCode;

    private String message;


    private T data;

    private List<Map<String, Object>> errors;
}