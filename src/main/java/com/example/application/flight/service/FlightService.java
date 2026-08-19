package com.example.application.flight.service;

import com.example.application.flight.dto.FlightSearchRequest;
import com.example.application.flight.dto.FlightSearchResponse;
import com.example.application.flight.dto.FlightSearchResult;
import com.example.application.flight.entity.Flight;


import java.time.LocalDate;
import java.util.List;

public interface FlightService {

    FlightSearchResult searchFlight(FlightSearchRequest request);
    FlightSearchResponse getFlightByFlightNumber(String flightNumber);
    List<FlightSearchResponse> getFlightsByAirline(String airline);
}
