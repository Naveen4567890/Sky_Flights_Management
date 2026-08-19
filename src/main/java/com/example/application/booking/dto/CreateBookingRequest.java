package com.example.application.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private Long onwardFlightId;

    private Long returnFlightId;

    private List<PassengerRequest> passengers;

    private String paymentMethod;

    private String paymentId;
}