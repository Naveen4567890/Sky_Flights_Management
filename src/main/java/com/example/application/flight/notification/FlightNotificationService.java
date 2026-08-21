package com.example.application.flight.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendPaymentNotification(
            NotificationMessage notification) {

        messagingTemplate.convertAndSend(
                "/topic/payment",
                notification
        );
    }

    public void sendBookingNotification(
            NotificationMessage notification) {

        messagingTemplate.convertAndSend(
                "/topic/booking",
                notification
        );
    }
}