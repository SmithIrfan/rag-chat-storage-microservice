package com.example.RAGChatMicroservice.constants;

/**
 * Constants for servlet filter configuration.
 */
public final class FilterConstants {

    private FilterConstants() {}

    public static final int REQUEST_LIMIT = 2;
    public static final int STATUS_TOO_MANY_REQUESTS = 429;
    public static final String ERROR_TOO_MANY_REQUESTS = "Too many requests. Please try again later.";

    public static final int ORDER_API_KEY_FILTER = 1;
    public static final int ORDER_RATE_LIMIT_FILTER = 2;
}
