package com.example.application.booking.service;

import com.example.application.booking.dto.BookingConfirmationDto;
import com.example.application.booking.dto.PassengerRequest;
import com.example.application.booking.entity.Passenger;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;


    public void sendBookingConfirmation(
            BookingConfirmationDto booking)
            throws MessagingException {

        // Create Thymeleaf context
        Context context = new Context();

        // Add booking object to template
        context.setVariable("booking", booking);


        // Process HTML template
        String htmlContent =
                templateEngine.process(
                        "booking-confirmation",
                        context
                );


        // Create email
        MimeMessage message =
                mailSender.createMimeMessage();


        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        true,
                        "UTF-8"
                );


        // Recipient
        helper.setTo(
                booking.getEmail()
        );

        for (PassengerRequest passenger : booking.getPassengers()) {

            if (!passenger.getEmail()
                    .equals(booking.getEmail())) {

                helper.addCc(passenger.getEmail());
            }
        }


        // Subject
        helper.setSubject(
                "✈ Flight Booking Confirmed - PNR "
                        + booking.getBookingReference()
        );


        // Send HTML email
        helper.setText(
                htmlContent,
                true
        );


        // Send
        mailSender.send(message);
    }
}