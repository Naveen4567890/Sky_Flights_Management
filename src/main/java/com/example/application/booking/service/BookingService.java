package com.example.application.booking.service;

import com.example.application.booking.dto.BookingConfirmationDto;
import com.example.application.booking.dto.CreateBookingRequest;
import com.example.application.booking.dto.CreateBookingResponse;
import com.example.application.booking.dto.PassengerRequest;
import com.example.application.booking.entity.Booking;
import com.example.application.booking.entity.FlightSeat;
import com.example.application.booking.entity.Passenger;
import com.example.application.booking.repository.BookingRepository;
import com.example.application.booking.repository.FlightSeatRepository;
import com.example.application.booking.repository.PassengerRepository;
import com.example.application.flight.entity.Flight;
import com.example.application.flight.repository.FlightRepository;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final NotificationService notificationService;


    @Transactional
    public CreateBookingResponse createBooking(
            CreateBookingRequest request
    ) {


        if (request == null) {
            throw new RuntimeException(
                    "Booking request cannot be null"
            );
        }

        if (request.getOnwardFlightId() == null) {
            throw new RuntimeException(
                    "Onward flight is required"
            );
        }

        if (request.getPassengers() == null
                || request.getPassengers().isEmpty()) {

            throw new RuntimeException(
                    "At least one passenger is required"
            );
        }




        Flight onwardFlight = flightRepository
                .findById(request.getOnwardFlightId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Onward flight not found"
                        )
                );


        Flight returnFlight = null;

        if (request.getReturnFlightId() != null) {

            returnFlight = flightRepository
                    .findById(request.getReturnFlightId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Return flight not found"
                            )
                    );
        }


        // ========================================================
        // 4. PASSENGER COUNT
        // ========================================================

        int passengerCount =
                request.getPassengers().size();



        if (onwardFlight.getAvailableSeats() == null
                || onwardFlight.getAvailableSeats() < passengerCount) {

            throw new RuntimeException(
                    "Not enough seats available on onward flight"
            );
        }


        if (returnFlight != null) {

            if (returnFlight.getAvailableSeats() == null
                    || returnFlight.getAvailableSeats()
                    < passengerCount) {

                throw new RuntimeException(
                        "Not enough seats available on return flight"
                );
            }
        }


        Set<String> onwardSeats = new HashSet<>();

        for (PassengerRequest passenger :
                request.getPassengers()) {

            String onwardSeat =
                    passenger.getOnwardSeatNumber();

            if (onwardSeat == null
                    || onwardSeat.isBlank()) {

                throw new RuntimeException(
                        "Onward seat is required for "
                                + passenger.getFirstName()
                );
            }

            if (!onwardSeats.add(onwardSeat)) {

                throw new RuntimeException(
                        "Duplicate onward seat selected: "
                                + onwardSeat
                );
            }
        }


        Set<String> returnSeats = new HashSet<>();

        if (returnFlight != null) {

            for (PassengerRequest passenger :
                    request.getPassengers()) {

                String returnSeat =
                        passenger.getReturnSeatNumber();

                if (returnSeat == null
                        || returnSeat.isBlank()) {

                    throw new RuntimeException(
                            "Return seat is required for "
                                    + passenger.getFirstName()
                    );
                }

                if (!returnSeats.add(returnSeat)) {

                    throw new RuntimeException(
                            "Duplicate return seat selected: "
                                    + returnSeat
                    );
                }
            }
        }


        BigDecimal onwardPrice =
                onwardFlight.getPrice() != null
                        ? onwardFlight.getPrice()
                        : BigDecimal.ZERO;

        BigDecimal returnPrice =
                returnFlight != null
                        && returnFlight.getPrice() != null
                        ? returnFlight.getPrice()
                        : BigDecimal.ZERO;

        BigDecimal totalAmount =
                onwardPrice
                        .add(returnPrice)
                        .multiply(
                                BigDecimal.valueOf(
                                        passengerCount
                                )
                        );


        String bookingReference =
                generateBookingReference();


        Booking booking = new Booking();

        booking.setBookingReference(
                bookingReference
        );

        booking.setOnwardFlight(
                onwardFlight
        );

        booking.setReturnFlight(
                returnFlight
        );

        booking.setPassengerCount(
                passengerCount
        );


        booking.setTotalAmount(
                totalAmount
        );

        booking.setPaymentMethod(
                request.getPaymentMethod()
        );

        booking.setPaymentId(
                request.getPaymentId()
        );

        booking.setStatus(
                "CONFIRMED"
        );

        booking.setBookingDate(
                LocalDateTime.now()
        );

        booking = bookingRepository.save(booking);


        Passenger primaryPassenger = null;

        List<Passenger> passengers = new ArrayList<>();

        for (PassengerRequest passengerRequest :
                request.getPassengers()) {

            Passenger passenger =
                    new Passenger();

            passenger.setBooking(
                    booking
            );

            passenger.setFirstName(
                    passengerRequest.getFirstName()
            );

            passenger.setLastName(
                    passengerRequest.getLastName()
            );
            passenger.setDateOfBirth(
                    passengerRequest.getDateOfBirth()
            );

            passenger.setEmail(
                    passengerRequest.getEmail()
            );

            passenger.setPhone(
                    passengerRequest.getPhone()
            );

            passenger.setOnwardSeatNumber(
                    passengerRequest.getOnwardSeatNumber()
            );
            passenger.setReturnSeatNumber(
                    passengerRequest.getReturnSeatNumber()
            );

            passenger.setCabin(
                    passengerRequest.getCabin()
            );

            passenger = passengerRepository.save(passenger);

            passengers.add(passenger);

            if (primaryPassenger == null) {
                primaryPassenger = passenger;
            }
        }



        onwardFlight.setAvailableSeats(
                onwardFlight.getAvailableSeats()
                        - passengerCount
        );

        flightRepository.save(
                onwardFlight
        );


        if (returnFlight != null) {

            returnFlight.setAvailableSeats(
                    returnFlight.getAvailableSeats()
                            - passengerCount
            );

            flightRepository.save(
                    returnFlight
            );
        }

        BookingConfirmationDto confirmationDto =
                convertToConfirmationDto(
                        booking,
                        primaryPassenger,
                        passengers
                );


        try {

            notificationService.sendBookingConfirmation(
                    confirmationDto
            );

        } catch (Exception e) {

            System.err.println(
                    "Notification failed: "
                            + e.getMessage()
            );
        }


        return new CreateBookingResponse(

                booking.getId(),

                booking.getBookingReference(),

                booking.getStatus(),

                "Booking created successfully"
        );
    }


    public Booking getBooking(
            String bookingReference
    ) {

        if (bookingReference == null
                || bookingReference.isBlank()) {

            throw new RuntimeException(
                    "Booking reference is required"
            );
        }

        return bookingRepository
                .findByBookingReference(
                        bookingReference
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );
    }

    private BookingConfirmationDto convertToConfirmationDto(
            Booking booking,
            Passenger passenger,
            List<Passenger> passengers
    ) {

        if (passenger == null) {

            throw new RuntimeException(
                    "Primary passenger not found"
            );
        }

        List<PassengerRequest> passengerDtos =
                passengers.stream()
                        .map(p -> PassengerRequest.builder()
                                .firstName(p.getFirstName())
                                .lastName(p.getLastName())
                                .email(p.getEmail())
                                .phone(p.getPhone())
                                .onwardSeatNumber(p.getOnwardSeatNumber())
                                .returnSeatNumber(p.getReturnSeatNumber())
                                .cabin(p.getCabin())
                                .build())
                        .toList();

        Flight flight =
                booking.getOnwardFlight();

        Flight returnFlight =
                booking.getReturnFlight();


        String passengerName =
                passenger.getFirstName()
                        + " "
                        + passenger.getLastName();


        return BookingConfirmationDto.builder()



                .bookingId(
                        booking.getId()
                )

                .bookingReference(
                        booking.getBookingReference()
                )




                .passengerName(
                        passengerName
                )

                .email(
                        passenger.getEmail()
                )

                .phone(
                        passenger.getPhone()
                )
                .passengers(passengerDtos)



                .airline(
                        flight.getAirline()
                )

                .flightNumber(
                        flight.getFlightNumber()
                )

                .source(
                        flight.getSource()
                )

                .destination(
                        flight.getDestination()
                )

                .travelDate(
                        flight.getTravelDate()
                )

                .departureTime(
                        flight.getDepartureTime()
                )

                .arrivalTime(
                        flight.getArrivalTime()
                )

                .returnAirline(
                        returnFlight != null
                                ? returnFlight.getAirline()
                                : null
                )

                .returnFlightNumber(
                        returnFlight != null
                                ? returnFlight.getFlightNumber()
                                : null
                )

                .returnSource(
                        returnFlight != null
                                ? returnFlight.getSource()
                                : null
                )

                .returnDestination(
                        returnFlight != null
                                ? returnFlight.getDestination()
                                : null
                )

                .returnTravelDate(
                        returnFlight != null
                                ? returnFlight.getTravelDate()
                                : null
                )

                .returnDepartureTime(
                        returnFlight != null
                                ? returnFlight.getDepartureTime()
                                : null
                )

                .returnArrivalTime(
                        returnFlight != null
                                ? returnFlight.getArrivalTime()
                                : null
                )


                .onwardSeatNumber(
                        passenger.getOnwardSeatNumber()
                )

                .returnSeatNumber(
                        passenger.getReturnSeatNumber()
                )



                .cabin(
                        flight.getCabin() != null
                                ? flight.getCabin().toString()
                                : passenger.getCabin()
                )


                .totalAmount(
                        booking.getTotalAmount()
                )

                .bookingStatus(
                        booking.getStatus()
                )

                .build();
    }



    private String generateBookingReference() {

        String date =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyyMMdd"
                                )
                        );

        String random =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(
                                0,
                                6
                        )
                        .toUpperCase();


        return "BK-"
                + date
                + "-"
                + random;
    }
}