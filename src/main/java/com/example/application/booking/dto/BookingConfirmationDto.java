package com.example.application.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "BookingConfirmation",
        description = "Complete booking confirmation containing passenger, flight, seat and payment information"
)
public class BookingConfirmationDto {

    // ============================================================
    // BOOKING
    // ============================================================

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


    // ============================================================
    // PASSENGER
    // ============================================================

    @Schema(
            description = "Primary passenger name",
            example = "Naveen Kumar"
    )
    private String passengerName;


    @Schema(
            description = "Passenger email address",
            example = "naveen@example.com"
    )
    private String email;


    @Schema(
            description = "Passenger phone number",
            example = "+919876543210"
    )
    private String phone;


    // ============================================================
    // ONWARD FLIGHT
    // ============================================================

    @Schema(
            description = "Airline operating the onward flight",
            example = "Air India"
    )
    private String airline;


    @Schema(
            description = "Onward flight number",
            example = "AI101"
    )
    private String flightNumber;


    @Schema(
            description = "Onward flight source airport",
            example = "DEL"
    )
    private String source;


    @Schema(
            description = "Onward flight destination airport",
            example = "BOM"
    )
    private String destination;


    @Schema(
            description = "Onward flight travel date",
            example = "2026-09-15"
    )
    private LocalDate travelDate;


    @Schema(
            description = "Scheduled onward flight departure time",
            example = "2026-09-15T10:30:00"
    )
    private LocalDateTime departureTime;


    @Schema(
            description = "Scheduled onward flight arrival time",
            example = "2026-09-15T12:45:00"
    )
    private LocalDateTime arrivalTime;


    // ============================================================
    // RETURN FLIGHT
    // ============================================================

    @Schema(
            description = "Return flight airline",
            example = "IndiGo"
    )
    private String returnAirline;


    @Schema(
            description = "Return flight number",
            example = "6E205"
    )
    private String returnFlightNumber;


    @Schema(
            description = "Return flight source airport",
            example = "BOM"
    )
    private String returnSource;


    @Schema(
            description = "Return flight destination airport",
            example = "DEL"
    )
    private String returnDestination;


    @Schema(
            description = "Return flight travel date",
            example = "2026-09-20"
    )
    private LocalDate returnTravelDate;


    @Schema(
            description = "Scheduled return flight departure time",
            example = "2026-09-20T18:30:00"
    )
    private LocalDateTime returnDepartureTime;


    @Schema(
            description = "Scheduled return flight arrival time",
            example = "2026-09-20T20:45:00"
    )
    private LocalDateTime returnArrivalTime;


    // ============================================================
    // SEATS
    // ============================================================

    @Schema(
            description = "Selected seat on the onward flight",
            example = "12A"
    )
    private String onwardSeatNumber;


    @Schema(
            description = "Selected seat on the return flight",
            example = "14C"
    )
    private String returnSeatNumber;


    // ============================================================
    // BOOKING
    // ============================================================

    @Schema(
            description = "Cabin class",
            example = "Economy"
    )
    private String cabin;


    @Schema(
            description = "Total amount paid for the booking",
            example = "10998.00"
    )
    private BigDecimal totalAmount;


    @Schema(
            description = "Current booking status",
            example = "CONFIRMED"
    )
    private String bookingStatus;


    @Schema(
            description = "List of all passengers included in the booking"
    )
    private List<PassengerRequest> passengers;
}