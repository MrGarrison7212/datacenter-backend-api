package com.example.datacenter_backend_api.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError(
        String code,
        String message,
        OffsetDateTime timestamp,
        String path,
        Map<String, String> details
) {
    public static ApiError of(String code, String message, String path) {
        return new ApiError(code, message, OffsetDateTime.now(), path, null);
    }

    public static ApiError of(String code, String message, String path, Map<String, String> details) {
        return new ApiError(code, message, OffsetDateTime.now(), path, details);
    }
}