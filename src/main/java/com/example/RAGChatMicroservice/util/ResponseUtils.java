package com.example.RAGChatMicroservice.util;

import com.example.RAGChatMicroservice.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for building consistent API responses.
 *
 * <p>
 * Provides helper methods to wrap responses in {@link ApiResponse}
 * with proper HTTP status codes, messages, and optional data payloads.
 * </p>
 */
public class ResponseUtils {

    /**
     * Default data value when none is provided.
     */
    private static final Object DEFAULT_DATA = null;

    private ResponseUtils() {
        // Prevent instantiation
    }

    /**
     * Builds a response with only status and message.
     *
     * @param statusCode the HTTP status code
     * @param message    the response message
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     */
    public static ResponseEntity<ApiResponse> getResponse(final HttpStatus statusCode, final String message) {
        return ResponseEntity.status(statusCode).body(new ApiResponse(statusCode.value(), message, DEFAULT_DATA));
    }

    /**
     * Builds a response with status, message, and data payload.
     *
     * @param statusCode the HTTP status code
     * @param message    the response message
     * @param data       the response data payload
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     */
    public static ResponseEntity<ApiResponse> getResponseEntity(final HttpStatus statusCode, final String message, final Object data) {
        return ResponseEntity.status(statusCode).body(new ApiResponse(statusCode.value(), message, data));
    }
}
