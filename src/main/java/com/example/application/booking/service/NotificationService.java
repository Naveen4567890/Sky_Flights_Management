package com.example.application.booking.service;

import com.example.application.booking.dto.BookingConfirmationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    public void sendBookingConfirmation(
            BookingConfirmationDto booking) {

        // Send email
        try {

            emailService.sendBookingConfirmation(booking);

            System.out.println(
                    "Booking confirmation email sent successfully"
            );

        } catch (Exception e) {

            System.err.println(
                    "Email sending failed: "
                            + e.getMessage()
            );
        }

    }
}