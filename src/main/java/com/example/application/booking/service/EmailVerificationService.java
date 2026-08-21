package com.example.application.booking.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;

    private final Map<String, OtpData> otpStorage =
            new ConcurrentHashMap<>();

    public EmailVerificationService(
            JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    public void sendOtp(String email) {

        String otp = String.format(
                "%06d",
                new Random().nextInt(1_000_000)
        );

        otpStorage.put(
                email,
                new OtpData(
                        otp,
                        System.currentTimeMillis()
                )
        );

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(
                "Sky Flights - Email Verification"
        );

        message.setText(
                "Your Sky Flights verification code is: "
                        + otp
                        + "\n\n"
                        + "This OTP is valid for 5 minutes."
        );

        mailSender.send(message);
    }

    public boolean verifyOtp(
            String email,
            String otp) {

        OtpData data = otpStorage.get(email);

        if (data == null) {
            return false;
        }

        // OTP expires after 5 minutes
        long currentTime =
                System.currentTimeMillis();

        if (currentTime - data.createdAt > 5 * 60 * 1000) {

            otpStorage.remove(email);

            return false;
        }

        if (!data.otp.equals(otp)) {
            return false;
        }

        // OTP successfully verified
        otpStorage.remove(email);

        return true;
    }

    private static class OtpData {

        private final String otp;
        private final long createdAt;

        public OtpData(
                String otp,
                long createdAt) {

            this.otp = otp;
            this.createdAt = createdAt;
        }
    }
}
