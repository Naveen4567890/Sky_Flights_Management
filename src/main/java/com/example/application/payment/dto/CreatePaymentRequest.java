package com.example.application.payment.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        BigDecimal amount,
        String paymentMethod
) {
}