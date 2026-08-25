package com.example.application.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(
        name = "FlightSearchResponse",
        description = "Flight information returned from the flight search API"
)
public class FlightSearchResponse {

    @Schema(
            description = "Unique flight ID",
            example = "101"
    )
    private Long id;

    @Schema(
            description = "Name of the airline",
            example = "Air India"
    )
    private String airline;

    @Schema(
            description = "Unique flight number",
            example = "AI101"
    )
    private String flightNumber;

    @Schema(
            description = "Source airport IATA code",
            example = "DEL"
    )
    private String source;

    @Schema(
            description = "Destination airport IATA code",
            example = "BOM"
    )
    private String destination;

    @Schema(
            description = "Scheduled departure time",
            example = "10:30"
    )
    private String departureTime;

    @Schema(
            description = "Scheduled arrival time",
            example = "12:45"
    )
    private String arrivalTime;

    @Schema(
            description = "Total flight duration",
            example = "2h 15m"
    )
    private String duration;

    @Schema(
            description = "Flight ticket price",
            example = "5499.00"
    )
    private BigDecimal price;

    @Schema(
            description = "Cabin class",
            example = "Economy"
    )
    private String cabin;

    @Schema(
            description = "Number of seats currently available",
            example = "42"
    )
    private Integer availableSeats;

    @Schema(
            description = "Date on which the flight operates",
            example = "2026-09-15"
    )
    private LocalDate travelDate;
}