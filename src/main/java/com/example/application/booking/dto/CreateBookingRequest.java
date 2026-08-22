package com.example.application.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private Long onwardFlightId;

    private Long returnFlightId;

    private List<PassengerRequest> passengers;

    private String paymentMethod;

    private String paymentId;

    private BigDecimal totalAmount;


}