package com.example.RAGChatMicroservice.security;

import com.example.RAGChatMicroservice.properties.RateLimitProperties;
import com.example.RAGChatMicroservice.util.FilterResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.RAGChatMicroservice.constants.FilterConstants.ERROR_TOO_MANY_REQUESTS;
import static com.example.RAGChatMicroservice.constants.FilterConstants.STATUS_TOO_MANY_REQUESTS;

/**
 * {@code RateLimitFilter} enforces a strict fixed-window rate limiting policy
 * on a per-API basis.
 *
 * <p>
 * Each API endpoint is assigned an independent rate-limit window identified
 * by the HTTP method and Spring's best-matching request pattern.
 * </p>
 *
 * <h3>Rate-Limiting Behavior</h3>
 * <ul>
 *   <li>Allows exactly {@code capacity} requests per API within a time window.</li>
 *   <li>The {@code capacity + 1} request is immediately rejected with HTTP 429.</li>
 *   <li>No token refill or burst tolerance is applied.</li>
 *   <li>The request counter resets only after the full window duration elapses.</li>
 * </ul>
 *
 * <p>
 * This implementation is suitable for single-instance deployments.
 * For horizontally scaled environments, a distributed store such as Redis
 * should be used to maintain consistency across instances.
 * </p>
 */
public class RateLimitFilter extends OncePerRequestFilter {

    /**
     * Maximum number of allowed requests per API per window
     */
    private final int capacity;

    /**
     * Fixed window duration in milliseconds
     */
    private final long windowDurationMillis;

    /**
     * Holds per-API rate-limit windows.
     * <p>
     * Key format:
     * <pre>
     * HTTP_METHOD:/api/path/{pattern}
     * </pre>
     * Example:
     * <pre>
     * GET:/api/chat/sessions
     * POST:/api/chat/messages
     * </pre>
     */
    private final Map<String, RateLimitWindow> apiWindows = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code RateLimitFilter} using configured rate-limit properties.
     *
     * @param rateLimitProperties configuration containing capacity and duration
     */
    public RateLimitFilter(RateLimitProperties rateLimitProperties) {
        this.capacity = rateLimitProperties.getCapacity();
        this.windowDurationMillis = rateLimitProperties.getDurationMinutes() * 60_000L;
    }

    /**
     * Applies strict per-API fixed-window rate-limiting logic.
     *
     * <p>
     * The API key is derived from the HTTP method and Spring's
     * {@link HandlerMapping#BEST_MATCHING_PATTERN_ATTRIBUTE}, ensuring that
     * logically identical endpoints (e.g. {@code /sessions/{id}}) share the
     * same rate-limit window.
     * </p>
     *
     * @param request     incoming HTTP request
     * @param response    HTTP response
     * @param filterChain filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Resolve the normalized API pattern for per-endpoint isolation
        Object patternAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        String apiKey = request.getMethod() + ":" + (patternAttr != null ? patternAttr.toString() : request.getRequestURI());

        long currentTimeMillis = System.currentTimeMillis();

        // Retrieve or create the rate-limit window for this API
        RateLimitWindow window = apiWindows.computeIfAbsent(apiKey, k -> new RateLimitWindow());

        synchronized (window) {

            // Reset the window if the duration has elapsed
            if (currentTimeMillis - window.windowStart >= windowDurationMillis) {
                window.windowStart = currentTimeMillis;
                window.counter.set(0);
            }

            // Enforce strict rate limit
            if (window.counter.incrementAndGet() > capacity) {
                FilterResponseUtil.writeJson(response, STATUS_TOO_MANY_REQUESTS, ERROR_TOO_MANY_REQUESTS);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Represents a single fixed-window rate-limit state for an API.
     */
    private static class RateLimitWindow {

        /**
         * Request counter for the current window
         */
        AtomicInteger counter = new AtomicInteger(0);

        /**
         * Window start timestamp in milliseconds
         */
        long windowStart = System.currentTimeMillis();
    }
}
