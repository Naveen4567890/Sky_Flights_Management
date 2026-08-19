package com.example.application.payment.dto;

public record VerifyPaymentRequest(
        String orderId,
        String paymentId,
        String signature,
        Long bookingId
) {
}