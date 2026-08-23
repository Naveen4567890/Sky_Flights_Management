

package com.example.application.booking.dto;

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
public class BookingConfirmationDto {

    private Long bookingId;
    private String bookingReference;

    // ============================================================
    // PASSENGER
    // ============================================================

    private String passengerName;
    private String email;
    private String phone;


    // ============================================================
    // ONWARD FLIGHT
    // ============================================================

    private String airline;
    private String flightNumber;
    private String source;
    private String destination;

    private LocalDate travelDate;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;


    // ============================================================
    // RETURN FLIGHT
    // ============================================================

    private String returnAirline;
    private String returnFlightNumber;
    private String returnSource;
    private String returnDestination;

    private LocalDate returnTravelDate;
    private LocalDateTime returnDepartureTime;
    private LocalDateTime returnArrivalTime;


    // ============================================================
    // SEATS
    // ============================================================

    private String onwardSeatNumber;
    private String returnSeatNumber;


    // ============================================================
    // BOOKING
    // ============================================================

    private String cabin;
    private BigDecimal totalAmount;
    private String bookingStatus;

    private List<PassengerRequest> passengers;
}