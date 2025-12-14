package com.example.RAGChatMicroservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.RAGChatMicroservice.constants.SecurityConstants.API_KEY_SCHEME;
import static com.example.RAGChatMicroservice.constants.SecurityConstants.HEADER_API_KEY;

/**
 * OpenAPI / Swagger configuration for API Key authentication.
 * <p>
 * This configuration ensures that the API documentation reflects
 * the requirement for clients to provide an API key in the request header.
 * </p>
 *
 * All endpoints documented in Swagger UI will display the requirement
 * to include the API key in the <code>X-API-KEY</code> header.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI specification with API key authentication.
     *
     * @return a customized {@link OpenAPI} instance with security requirements
     */
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme apiKeyScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(HEADER_API_KEY);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(API_KEY_SCHEME, apiKeyScheme))
                .addSecurityItem(new SecurityRequirement().addList(API_KEY_SCHEME));
    }

}
