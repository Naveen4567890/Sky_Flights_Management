package com.example.application.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "CreatePaymentResponse",
        description = "Payment order details returned by the payment service"
)
public record CreatePaymentResponse(

        @Schema(
                description = "Payment gateway order ID",
                example = "order_P7X82K91AB"
        )
        String orderId,

        @Schema(
                description = "Payment gateway public key ID used by the frontend",
                example = "rzp_test_ABC123456"
        )
        String keyId,

        @Schema(
                description = "Payment amount",
                example = "10998.00"
        )
        BigDecimal amount,

        @Schema(
                description = "Currency used for the payment",
                example = "INR"
        )
        String currency
) {
}