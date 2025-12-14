package com.example.RAGChatMicroservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper.
 *
 * <p>
 * Provides a consistent structure for all REST responses,
 * including success indicator, message, and optional data payload.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    /** Indicator of success  */
    private int success;

    /** Human-readable message describing the result. */
    private String message;

    /** Optional data payload returned by the API. */
    private Object data;
}
