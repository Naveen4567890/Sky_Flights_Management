package com.example.application.flight.dto;

import com.example.application.flight.enums.CabinClass;
import com.example.application.flight.enums.TripType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FlightSearchRequest {

    private TripType tripType;

    private String source;

    private String destination;

    private LocalDate departureDate;

    private LocalDate returnDate;

    private String cabin;

    private Traveller traveller;
}
