package com.example.application.flight.notification;

public record NotificationMessage(
        String type,
        String message,
        String paymentStatus
) {
}
