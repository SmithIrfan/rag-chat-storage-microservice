package com.example.RAGChatMicroservice.security;

import com.example.RAGChatMicroservice.util.FilterResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.RAGChatMicroservice.constants.SecurityConstants.ERROR_INVALID_API_KEY;
import static com.example.RAGChatMicroservice.constants.SecurityConstants.HEADER_API_KEY;

/**
 * Servlet filter for API key authentication.
 *
 * <p>
 * Validates incoming requests by checking the {@code X-API-KEY} header
 * against the configured API key. If the key is missing or invalid,
 * the request is rejected with a JSON response and HTTP 401 (Unauthorized).
 * </p>
 */
public class ApiKeyAuthFilter extends OncePerRequestFilter {



    /**
     * The API key configured in application properties.
     */
    private final String configuredApiKey;

    /**
     * Constructs the filter with the configured API key.
     *
     * @param configuredApiKey the expected API key value
     */
    public ApiKeyAuthFilter(String configuredApiKey) {
        this.configuredApiKey = configuredApiKey;
    }

    /**
     * Performs API key validation for each request.
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to continue processing if valid
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader(HEADER_API_KEY);

        if (requestApiKey == null || !requestApiKey.equals(configuredApiKey)) {
            FilterResponseUtil.writeJson(response, HttpStatus.UNAUTHORIZED.value(), // 401 Unauthorized
                    ERROR_INVALID_API_KEY);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
