package com.example.application.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmationDto {

    private Long bookingId;
    private String bookingReference;

    // Passenger
    private String passengerName;
    private String email;
    private String phone;

    // Flight
    private String airline;
    private String flightNumber;
    private String source;
    private String destination;

    private LocalDate travelDate;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    // Booking
    private String onwardSeatNumber;

    private String returnSeatNumber;
    private String cabin;
    private BigDecimal totalAmount;
    private String bookingStatus;
}