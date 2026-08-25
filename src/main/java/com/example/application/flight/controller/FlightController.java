package com.example.application.flight.controller;

import com.example.application.flight.dto.FlightSearchRequest;
import com.example.application.flight.dto.FlightSearchResponse;
import com.example.application.flight.dto.FlightSearchResult;
import com.example.application.flight.service.FlightServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@Tag(
        name = "Flight Management",
        description = "APIs for searching and retrieving flight information"
)
public class FlightController {

    private final FlightServiceImpl flightService;


    // ============================================================
    // GET FLIGHT BY FLIGHT NUMBER
    // ============================================================

    @Operation(
            summary = "Get flight by flight number",
            description = "Retrieves flight details using the flight number."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Flight found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Flight not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/flight-number/{flightNumber}")
    public FlightSearchResponse getFlightByFlightNumber(

            @Parameter(
                    description = "Unique flight number",
                    example = "AI101",
                    required = true
            )
            @PathVariable String flightNumber) {

        return flightService.getFlightByFlightNumber(flightNumber);
    }


    // ============================================================
    // GET FLIGHTS BY AIRLINE
    // ============================================================

    @Operation(
            summary = "Get flights by airline",
            description = "Retrieves all available flights operated by the specified airline."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Flights retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No flights found for the airline"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/airline/{airline}")
    public List<FlightSearchResponse> getFlightsByAirline(

            @Parameter(
                    description = "Airline name",
                    example = "Air India",
                    required = true
            )
            @PathVariable String airline) {

        return flightService.getFlightsByAirline(airline);
    }


    // ============================================================
    // SEARCH FLIGHTS
    // ============================================================

    @Operation(
            summary = "Search available flights",
            description = """
                    Searches for available flights based on:
                    trip type, source airport, destination airport,
                    departure date, return date, cabin class,
                    and traveller information.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Flight search completed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid flight search request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No flights found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/search")
    public FlightSearchResult searchFlights(
            @RequestBody FlightSearchRequest request) {

        return flightService.searchFlight(request);
    }
}