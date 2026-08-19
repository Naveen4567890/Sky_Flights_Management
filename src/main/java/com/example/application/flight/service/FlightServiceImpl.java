package com.example.application.flight.service;

import com.example.application.flight.dto.FlightSearchRequest;
import com.example.application.flight.dto.FlightSearchResponse;
import com.example.application.flight.dto.FlightSearchResult;
import com.example.application.flight.entity.Flight;
import com.example.application.flight.enums.TripType;
import com.example.application.flight.mapper.FlightMapper;
import com.example.application.flight.repository.FlightRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;



@Service

@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService{

    private  final FlightRepository flightRepository;

    @Override
    public FlightSearchResult searchFlight(FlightSearchRequest request) {

        validateRequest(request);


        switch (request.getTripType()) {

            case ONE_WAY:
                return searchOneWay(request);

            case ROUND_TRIP:
                return searchRoundTrip(request);

            case MULTI_CITY:
                return searchMultiCity(request);

            default:
                throw new RuntimeException("Invalid Trip Type");
        }

        }

    private FlightSearchResult searchOneWay(FlightSearchRequest request) {
        List<Flight> flights =
                flightRepository.findBySourceIgnoreCaseAndDestinationIgnoreCaseAndTravelDateAndCabin(

                        request.getSource(),
                        request.getDestination(),
                        request.getDepartureDate(),
                        request.getCabin()
                );


        List<FlightSearchResponse> responses = flights.stream()
                .map(FlightMapper::map).toList();

        FlightSearchResult result = new FlightSearchResult();
        result.setOnwardFlights(responses);
        result.setReturnFlights(Collections.emptyList());

        return result;
    }

    private FlightSearchResult searchRoundTrip(FlightSearchRequest request) {

        List<Flight> onwardFlights =
                flightRepository
                        .findBySourceIgnoreCaseAndDestinationIgnoreCaseAndTravelDateAndCabin(

                                request.getSource(),
                                request.getDestination(),
                                request.getDepartureDate(),
                                request.getCabin()
                        );

        List<Flight> returnFlights =
                flightRepository
                        .findBySourceIgnoreCaseAndDestinationIgnoreCaseAndTravelDateAndCabin(

                                request.getDestination(),
                                request.getSource(),
                                request.getReturnDate(),
                                request.getCabin()
                        );
        List<FlightSearchResponse> onward =
                onwardFlights.stream()
                        .map(FlightMapper::map)
                        .toList();

        List<FlightSearchResponse> returns =
                returnFlights.stream()
                        .map(FlightMapper::map)
                        .toList();
        FlightSearchResult result = new FlightSearchResult();

        result.setOnwardFlights(onward);

        result.setReturnFlights(returns);

        return result;
    }


    private FlightSearchResult searchMultiCity(FlightSearchRequest request) {
        throw new UnsupportedOperationException(
                "MultiCity Trip Search Coming Soon");
    }


    @Override
    public FlightSearchResponse getFlightByFlightNumber(String flightNumber) {

        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("Flight Not Found"));

        return FlightMapper.map(flight);
    }





    @Override
    public List<FlightSearchResponse> getFlightsByAirline(String airline) {

        List<Flight> flights = flightRepository.findByAirlineIgnoreCase(airline);

        return flights.stream()
                .map(FlightMapper::map)
                .toList();

    }

    private void validateRequest(FlightSearchRequest request){

        if(request.getSource().equalsIgnoreCase(request.getDestination())){

            throw new RuntimeException(
                    "Source and Destination cannot be same");

        }

        if(request.getTripType()== TripType.ROUND_TRIP &&
                request.getReturnDate()==null){

            throw new RuntimeException(
                    "Return Date is mandatory for Round Trip");

        }

        if(request.getTripType()==TripType.ONE_WAY &&
                request.getReturnDate()!=null){

            throw new RuntimeException(
                    "Return Date should be empty for One Way");

        }

//        if(request.getTraveller().getAdults()==0){
//
//            throw new RuntimeException(
//                    "At least one adult is required");
//
//        }
//
//        if(request.getTraveller().getInfants() >
//                request.getTraveller().getAdults()){
//
//            throw new RuntimeException(
//                    "Infants cannot exceed adults");
//
//        }


    }
}
