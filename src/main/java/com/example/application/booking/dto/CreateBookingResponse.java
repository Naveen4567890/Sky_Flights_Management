package com.example.application.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "CreateBookingResponse",
        description = "Response returned after creating a flight booking"
)
public class CreateBookingResponse {

    @Schema(
            description = "Unique booking ID",
            example = "1001"
    )
    private Long bookingId;


    @Schema(
            description = "Unique booking reference / PNR",
            example = "PNR7X82K"
    )
    private String bookingReference;


    @Schema(
            description = "Current booking status",
            example = "CONFIRMED"
    )
    private String status;


    @Schema(
            description = "Message describing the booking result",
            example = "Flight booking created successfully"
    )
    private String message;
}