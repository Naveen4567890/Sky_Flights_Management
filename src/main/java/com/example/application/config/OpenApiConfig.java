package com.example.application.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Flight Management System API",
                version = "1.0.0",
                description = """
                        REST API for managing flights, passengers,
                        bookings, seat selection and flight search.
                        """,
                contact = @Contact(
                        name = "Flight Management Team",
                        email = "support@flightmanagement.com"
                ),
                license = @License(
                        name = "Apache 2.0"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Enter JWT token"
)
public class OpenApiConfig {
}
