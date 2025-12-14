package com.example.RAGChatMicroservice.constants;

/**
 * Constants for API key authentication and security.
 */
public final class SecurityConstants {

    private SecurityConstants() {}
    /**
     * Name of the HTTP header used for API key authentication.
     */
    public static final String API_KEY_SCHEME = "ApiKeyAuth";
    public static final String HEADER_API_KEY = "X-API-KEY";
    public static final String ERROR_INVALID_API_KEY = "Invalid or missing API key";

    public static final String PROP_SECURITY_API_KEY = "security.api-key";
    public static final String URL_PATTERN_SESSIONS = "/v1/vp/sessions/*";
}
