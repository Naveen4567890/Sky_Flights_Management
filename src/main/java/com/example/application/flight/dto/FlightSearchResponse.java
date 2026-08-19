package com.example.application.flight.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FlightSearchResponse {

    private Long id;

    private String airline;

    private String flightNumber;

    private String source;

    private String destination;

    private String departureTime;

    private String arrivalTime;

    private String duration;

    private BigDecimal price;

    private String cabin;

    private Integer availableSeats;
}
