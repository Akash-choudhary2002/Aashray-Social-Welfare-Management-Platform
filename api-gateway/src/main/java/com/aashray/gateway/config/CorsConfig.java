package com.aashray.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * BUG FIX (audit): the Gateway previously had no CORS configuration at
 * all. Every downstream microservice has its own CorsConfigurationSource,
 * but that's irrelevant here — the browser only ever talks to the
 * Gateway directly (port 8080), so without CORS headers coming back
 * from the Gateway itself, every preflighted request from the React
 * frontend (POST/PUT/PATCH/DELETE with a JSON body, or any request
 * carrying an Authorization header) was being blocked by the browser
 * before it ever reached a microservice. This is the reactive
 * (WebFlux) equivalent of the CorsConfigurationSource bean used in the
 * individual services — Spring Cloud Gateway does not run on the
 * Servlet stack, so the servlet-side CorsFilter/CorsConfigurationSource
 * classes do not apply here and must not be used.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
