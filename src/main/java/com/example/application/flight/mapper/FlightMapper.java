package com.example.application.flight.mapper;

import com.example.application.flight.dto.FlightSearchResponse;
import com.example.application.flight.entity.Flight;

public class FlightMapper {

    public static FlightSearchResponse map(Flight flight) {


        return FlightSearchResponse.builder()
                .id(flight.getId())
                .airline(flight.getAirline())
                .flightNumber(flight.getFlightNumber())
                .source(flight.getSource())
                .destination(flight.getDestination())
                .departureTime(
                        flight.getDepartureTime()
                                .toLocalTime()
                                .toString()
                )
                .arrivalTime(
                        flight.getArrivalTime()
                                .toLocalTime()
                                .toString()
                )
                .duration(flight.getDuration())
                .price(flight.getPrice())
                .cabin(flight.getCabin())
                .availableSeats(flight.getAvailableSeats())
                .travelDate(flight.getTravelDate())
                .build();
    }
}
