package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final String[] allowedOrigins;
    private final String[] allowedMethods;
    private final String[] allowedHeaders;
    private final String[] exposedHeaders;
    private final boolean allowCredentials;

    public CorsConfig(
        @Value("${app.cors.allowed-origins}") String[] allowedOrigins,
        @Value("${app.cors.allowed-methods}") String[] allowedMethods,
        @Value("${app.cors.allowed-headers}") String[] allowedHeaders,
        @Value("${app.cors.exposed-headers}") String[] exposedHeaders,
        @Value("${app.cors.allow-credentials}") boolean allowCredentials
    ) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.exposedHeaders = exposedHeaders;
        this.allowCredentials = allowCredentials;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods(allowedMethods)
            .allowedHeaders(allowedHeaders)
            .exposedHeaders(exposedHeaders)
            .allowCredentials(allowCredentials);
    }
}
