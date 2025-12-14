package com.example.RAGChatMicroservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private int capacity;
    private int durationMinutes;

}
