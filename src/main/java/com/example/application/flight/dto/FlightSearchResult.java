package com.example.application.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        name = "FlightSearchResult",
        description = "Result returned after searching for available flights"
)
public class FlightSearchResult {

    @Schema(
            description = "List of onward flights. " +
                    "For one-way trips, this contains the available flights. " +
                    "For round trips, this contains the outbound flights.",
            implementation = FlightSearchResponse.class
    )
    private List<FlightSearchResponse> onwardFlights;


    @Schema(
            description = "List of return flights. " +
                    "This is populated for round-trip searches and may be empty for one-way trips.",
            implementation = FlightSearchResponse.class
    )
    private List<FlightSearchResponse> returnFlights;
}