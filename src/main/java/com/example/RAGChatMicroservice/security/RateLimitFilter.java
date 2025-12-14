package com.example.RAGChatMicroservice.security;

import com.example.RAGChatMicroservice.properties.RateLimitProperties;
import com.example.RAGChatMicroservice.util.FilterResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.RAGChatMicroservice.constants.FilterConstants.ERROR_TOO_MANY_REQUESTS;
import static com.example.RAGChatMicroservice.constants.FilterConstants.STATUS_TOO_MANY_REQUESTS;

/**
 * {@code RateLimitFilter} enforces a strict fixed-window rate limiting policy.
 *
 * <p>
 * This filter allows a maximum number of requests (configured capacity)
 * within a fixed time window (configured duration in minutes).
 * </p>
 *
 * <h3>Behavior</h3>
 * <ul>
 *   <li>Allows exactly {@code capacity} requests per time window.</li>
 *   <li>The {@code capacity + 1} request is rejected immediately with HTTP 429.</li>
 *   <li>No token refill or burst tolerance is applied.</li>
 *   <li>The request counter resets only after the full window duration elapses.</li>
 * </ul>
 *
 * <p>
 * This implementation is suitable for single-instance deployments.
 * For distributed environments, a shared store (e.g., Redis) should be used.
 * </p>
 */
public class RateLimitFilter extends OncePerRequestFilter {

    /**
     * Maximum allowed requests per window
     */
    private final int capacity;

    /**
     * Window duration in milliseconds
     */
    private final long windowDurationMillis;

    /**
     * Request counter for the current window
     */
    private final AtomicInteger requestCounter = new AtomicInteger(0);

    /**
     * Start timestamp of the current window
     */
    private volatile long windowStartTimeMillis;

    /**
     * Constructs the {@code RateLimitFilter} using application rate limit properties.
     *
     * @param rateLimitProperties configuration containing capacity and duration
     */
    public RateLimitFilter(RateLimitProperties rateLimitProperties) {
        this.capacity = rateLimitProperties.getCapacity();
        this.windowDurationMillis = rateLimitProperties.getDurationMinutes() * 60_000L;
        this.windowStartTimeMillis = System.currentTimeMillis();
    }

    /**
     * Applies strict fixed-window rate limiting logic.
     *
     * @param request     incoming HTTP request
     * @param response    HTTP response
     * @param filterChain filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long currentTimeMillis = System.currentTimeMillis();

        synchronized (this) {

            // Reset the window if the duration has elapsed
            if (currentTimeMillis - windowStartTimeMillis >= windowDurationMillis) {
                windowStartTimeMillis = currentTimeMillis;
                requestCounter.set(0);
            }

            // Increment request count and enforce strict limit
            if (requestCounter.incrementAndGet() > capacity) {
                FilterResponseUtil.writeJson(response, STATUS_TOO_MANY_REQUESTS, ERROR_TOO_MANY_REQUESTS);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
