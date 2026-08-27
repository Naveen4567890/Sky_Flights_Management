package com.example.application.user.security;

import com.example.application.user.entity.User;
import com.example.application.user.repository.UserRepository;
import com.example.application.user.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser.getAttribute("email");

        String name =
                oauthUser.getAttribute("name");

        System.out.println("Google Email: " + email);
        System.out.println("Google Name: " + name);

        // Find existing user
        User user = userRepository
                .findByEmail(email)
                .orElseGet(() -> {

                    User newUser = new User();

                    newUser.setEmail(email);
                    newUser.setName(name);

                    return userRepository.save(newUser);
                });

        // Generate your existing JWT
        String token =
                jwtService.generateToken(user.getEmail());
        String encodedToken =
                URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(
                "http://localhost:5173/oauth-success?token="
                        + encodedToken
        );
    }
}
