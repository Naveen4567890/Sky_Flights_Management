package com.example.application.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "PassengerRequest",
        description = "Passenger and seat information used during flight booking"
)
public class PassengerRequest {

    @Schema(
            description = "Passenger's first name",
            example = "Naveen",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;


    @Schema(
            description = "Passenger's last name",
            example = "Kumar",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;


    @Schema(
            description = "Passenger's email address",
            example = "naveen@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;


    @Schema(
            description = "Passenger's phone number",
            example = "+919876543210",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phone;


    @Schema(
            description = "Passenger's date of birth",
            example = "1998-05-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate dateOfBirth;


    @Schema(
            description = "Seat number selected on the onward flight",
            example = "12A"
    )
    private String onwardSeatNumber;


    @Schema(
            description = "Seat number selected on the return flight. " +
                    "Should be null for one-way bookings.",
            example = "14C"
    )
    private String returnSeatNumber;


    @Schema(
            description = "Cabin class selected for the passenger",
            example = "Economy"
    )
    private String cabin;
}