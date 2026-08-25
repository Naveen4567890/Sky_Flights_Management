package com.example.application.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "CreatePaymentRequest",
        description = "Request used to create a payment order"
)
public record CreatePaymentRequest(

        @Schema(
                description = "Payment amount",
                example = "10998.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal amount,

        @Schema(
                description = "Payment method",
                example = "RAZORPAY",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String paymentMethod
) {
}