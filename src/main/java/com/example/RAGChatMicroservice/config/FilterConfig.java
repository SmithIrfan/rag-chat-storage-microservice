package com.example.RAGChatMicroservice.config;

import com.example.RAGChatMicroservice.properties.RateLimitProperties;
import com.example.RAGChatMicroservice.security.ApiKeyAuthFilter;
import com.example.RAGChatMicroservice.security.RateLimitFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.RAGChatMicroservice.constants.FilterConstants.ORDER_API_KEY_FILTER;
import static com.example.RAGChatMicroservice.constants.FilterConstants.ORDER_RATE_LIMIT_FILTER;
import static com.example.RAGChatMicroservice.constants.SecurityConstants.PROP_SECURITY_API_KEY;
import static com.example.RAGChatMicroservice.constants.SecurityConstants.URL_PATTERN_SESSIONS;

/**
 * Configuration class for registering servlet filters.
 * <p>
 * This ensures that security-related filters (API key authentication and rate limiting)
 * are consistently applied to protected endpoints in the application.
 * </p>
 *
 */
@Configuration
public class FilterConfig {
    private final RateLimitProperties rateLimitProperties;

    public FilterConfig(RateLimitProperties rateLimitProperties) {
        this.rateLimitProperties = rateLimitProperties;
    }

    @Value("${" + PROP_SECURITY_API_KEY + "}")
    private String apiKey;

    /**
     * Registers the {@link ApiKeyAuthFilter} to enforce API key authentication.
     *
     * @return the filter registration bean for API key authentication
     */
    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiKeyAuthFilter(apiKey));
        registration.addUrlPatterns(URL_PATTERN_SESSIONS);
        registration.setOrder(ORDER_API_KEY_FILTER);
        return registration;
    }

    /**
     * Registers the {@link RateLimitFilter} to enforce request rate limiting.
     *
     * @return the filter registration bean for rate limiting
     */
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitFilter(rateLimitProperties));
        registration.addUrlPatterns(URL_PATTERN_SESSIONS);
        registration.setOrder(ORDER_RATE_LIMIT_FILTER);
        return registration;
    }
}
