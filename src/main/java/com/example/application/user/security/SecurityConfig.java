package com.example.application.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {

        http

                // ==========================================
                // CSRF
                // ==========================================

                // JWT REST API + WebSocket
                // does not use browser session-based CSRF
                .csrf(csrf -> csrf.disable())

                // ==========================================
                // CORS
                // ==========================================

                .cors(cors -> {})

                // ==========================================
                // AUTHORIZATION
                // ==========================================

                .authorizeHttpRequests(auth -> auth

                        // AUTH
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // WEBSOCKET / SOCKJS
                        .requestMatchers(
                                "/ws/**"
                        ).permitAll()

                        // FLIGHT
                        .requestMatchers(
                                "/api/flight/**"
                        ).permitAll()

                        // PAYMENT
                        .requestMatchers(
                                "/api/payment/**"
                        ).permitAll()

                        // BOOKING
                        .requestMatchers(
                                "/api/booking/**"
                        ).permitAll()

                        // OPTIONS / CORS PREFLIGHT
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                                .permitAll()

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                // ==========================================
                // STATELESS
                // ==========================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================================
                // JWT FILTER
                // ==========================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ==========================================
    // CORS CONFIGURATION
    // ==========================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5174"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}