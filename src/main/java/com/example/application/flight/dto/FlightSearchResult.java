package com.example.application.flight.dto;

import lombok.Data;

import java.util.List;
@Data
public class FlightSearchResult {
    // For One Way
    private List<FlightSearchResponse> onwardFlights;

    // For Round Trip
    private List<FlightSearchResponse> returnFlights;
}
