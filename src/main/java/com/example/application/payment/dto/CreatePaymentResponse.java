package com.example.application.payment.dto;

import java.math.BigDecimal;

public record CreatePaymentResponse(
        String orderId,
        String keyId,
        BigDecimal amount,
        String currency
) {
}