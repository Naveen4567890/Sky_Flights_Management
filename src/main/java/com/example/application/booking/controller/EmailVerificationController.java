package com.example.application.booking.controller;


import com.example.application.booking.dto.EmailOtpRequest;
import com.example.application.booking.dto.VerifyOtpRequest;
import com.example.application.booking.service.EmailVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class EmailVerificationController {

    private final EmailVerificationService
            emailVerificationService;

    public EmailVerificationController(
            EmailVerificationService emailVerificationService) {

        this.emailVerificationService =
                emailVerificationService;
    }

    @PostMapping("/send-email-otp")
    public ResponseEntity<?> sendOtp(
            @RequestBody EmailOtpRequest request) {

        emailVerificationService.sendOtp(
                request.getEmail()
        );

        return ResponseEntity.ok(
                "OTP sent successfully"
        );
    }

    @PostMapping("/verify-email-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        boolean verified =
                emailVerificationService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                );

        if (!verified) {

            return ResponseEntity.badRequest()
                    .body("Invalid or expired OTP");
        }

        return ResponseEntity.ok(
                "Email verified successfully"
        );
    }
}