package com.example.application.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "CreateBookingRequest",
        description = "Request payload used to create a flight booking"
)
public class CreateBookingRequest {

    @Schema(
            description = "ID of the selected onward flight",
            example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long onwardFlightId;


    @Schema(
            description = "ID of the selected return flight. " +
                    "Should be null for one-way bookings.",
            example = "205"
    )
    private Long returnFlightId;


    @Schema(
            description = "List of passengers included in the booking",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<PassengerRequest> passengers;


    @Schema(
            description = "Payment method used for the booking",
            example = "CARD",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String paymentMethod;


    @Schema(
            description = "Payment transaction ID returned by the payment gateway",
            example = "pay_ABC123456"
    )
    private String paymentId;


    @Schema(
            description = "Total amount for the booking",
            example = "10998.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal totalAmount;
}