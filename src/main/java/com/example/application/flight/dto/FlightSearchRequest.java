package com.example.application.flight.dto;

import com.example.application.flight.enums.TripType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(
        name = "FlightSearchRequest",
        description = "Request object used to search available flights"
)
public class FlightSearchRequest {

    @Schema(
            description = "Type of trip",
            example = "ROUND_TRIP",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private TripType tripType;


    @Schema(
            description = "Source airport IATA code",
            example = "DEL",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String source;


    @Schema(
            description = "Destination airport IATA code",
            example = "BOM",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String destination;


    @Schema(
            description = "Departure date",
            example = "2026-09-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate departureDate;


    @Schema(
            description = "Return date. Required for round-trip flights.",
            example = "2026-09-20"
    )
    private LocalDate returnDate;


    @Schema(
            description = "Cabin class",
            example = "Economy"
    )
    private String cabin;


    @Schema(
            description = "Traveller information",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Traveller traveller;
}
