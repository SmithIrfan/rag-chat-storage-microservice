package com.example.RAGChatMicroservice.security;

import com.example.RAGChatMicroservice.properties.RateLimitProperties;
import com.example.RAGChatMicroservice.util.FilterResponseUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.RAGChatMicroservice.constants.FilterConstants.STATUS_TOO_MANY_REQUESTS;
import static jakarta.servlet.RequestDispatcher.ERROR_MESSAGE;

/**
 * Servlet filter for rate limiting using Bucket4j.
 *
 * <p>
 * Restricts the number of requests per client IP within a given time window.
 * If the limit is exceeded, the request is rejected with HTTP 429 (Too Many Requests).
 * </p>
 */

public class RateLimitFilter extends OncePerRequestFilter {


    /**
     * Map of client IPs to their respective rate limit buckets.
     */
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final RateLimitProperties properties;

    public RateLimitFilter(RateLimitProperties properties) {
        this.properties = properties;
    }


    /**
     * Creates a new rate limit bucket with the configured limit and duration.
     *
     * @return a new {@link Bucket} instance
     */
    private Bucket createBucket() {
        return Bucket.builder().addLimit(Bandwidth.classic(properties.getCapacity(), Refill.greedy(properties.getCapacity(), Duration.ofMinutes(properties.getDurationMinutes())))).build();
    }

    /**
     * Applies rate limiting for each incoming request based on client IP.
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to continue processing if allowed
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String clientIp = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(clientIp, key -> createBucket());

        if (!bucket.tryConsume(1)) {
            FilterResponseUtil.writeJson(response, STATUS_TOO_MANY_REQUESTS, ERROR_MESSAGE);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
