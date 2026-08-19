package com.example.application.flight.controller;

import com.example.application.flight.dto.FlightSearchRequest;
import com.example.application.flight.dto.FlightSearchResponse;
import com.example.application.flight.dto.FlightSearchResult;
import com.example.application.flight.service.FlightServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/flight")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class FlightController {

    private final FlightServiceImpl flightService;

    //It will get the flights based on from , to, date , remaining details optional
    /*@GetMapping("/search")
    public List<FlightSearchResponse> searchFlights(@RequestParam String source,
                                                    @RequestParam String destination,
                                                    @RequestParam LocalDate date,

                                                    @RequestParam(required = false) String airline,
                                                    @RequestParam(required = false) String cabin,
                                                    @RequestParam(required = false) Integer stops,
                                                    @RequestParam(required = false) BigDecimal minPrice,
                                                    @RequestParam(required = false) BigDecimal maxPrice
                                                    ){

        FlightSearchRequest request = new FlightSearchRequest();

        request.setSource(source);
        request.setDestination(destination);
        request.setTravelDate(date);
        request.setAirline(airline);
        request.setCabin(cabin);
        request.setStops(stops);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);

        return flightService.searchFlight(request);

    }*/

    //It will get the flight based on flight number
    @GetMapping("/flight-number/{flightNumber}")
    public FlightSearchResponse getFlightByFlightNumber(
            @PathVariable String flightNumber) {

        return flightService.getFlightByFlightNumber(flightNumber);
    }

    //It will get the flight based on airline
    @GetMapping("/airline/{airline}")
    public List<FlightSearchResponse> getFlightsByAirline(
            @PathVariable String airline) {

        return flightService.getFlightsByAirline(airline);
    }

    @PostMapping("/search")
    public FlightSearchResult searchFlights(
            @RequestBody FlightSearchRequest request){

        return flightService.searchFlight(request);

    }

}
