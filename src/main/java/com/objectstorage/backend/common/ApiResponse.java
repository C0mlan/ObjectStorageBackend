package com.objectstorage.backend.common;

import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;

    // success payload
    private T data;

    // error details
    private List<Map<String, Object>> errors;
}