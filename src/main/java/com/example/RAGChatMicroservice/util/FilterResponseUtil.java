package com.example.RAGChatMicroservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.RAGChatMicroservice.constants.ResponseConstants.*;

/**
 * Utility class for writing consistent JSON responses from filters.
 * <p>
 * This class centralizes JSON response creation to ensure uniform structure
 * across authentication, authorization, and other servlet filters.
 * </p>
 *
 */
public final class FilterResponseUtil {

    /**
     * Shared ObjectMapper instance for JSON serialization.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private FilterResponseUtil() {
        // Prevent instantiation
    }

    /**
     * Writes a JSON response with a consistent structure.
     *
     * @param response the {@link HttpServletResponse} to write to
     * @param status   the HTTP status code (e.g., 200, 401, 403)
     * @param message  the message to include in the response body
     * @throws IOException if writing to the response fails
     */
    public static void writeJson(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put(FIELD_STATUS, status);
        body.put(FIELD_MESSAGE, message);
        body.put(FIELD_DATA, null);

        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(body));
    }
}
