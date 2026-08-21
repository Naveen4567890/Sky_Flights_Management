package com.example.application.booking.service;

import com.example.application.booking.dto.BookingConfirmationDto;
import com.example.application.booking.dto.CreateBookingRequest;
import com.example.application.booking.dto.CreateBookingResponse;
import com.example.application.booking.dto.PassengerRequest;
import com.example.application.booking.entity.Booking;
import com.example.application.booking.entity.Passenger;
import com.example.application.booking.repository.BookingRepository;
import com.example.application.booking.repository.PassengerRepository;

import com.example.application.flight.entity.Flight;
import com.example.application.flight.repository.FlightRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final NotificationService notificationService;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public BookingService(
            BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            FlightRepository flightRepository,
            NotificationService notificationService
    ) {

        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.flightRepository = flightRepository;
        this.notificationService = notificationService;
    }


    // ============================================================
    // CREATE BOOKING
    // ============================================================

    @Transactional
    public CreateBookingResponse createBooking(
            CreateBookingRequest request
    ) {

        // ========================================================
        // VALIDATION
        // ========================================================

        if (request == null) {

            throw new RuntimeException(
                    "Booking request cannot be null"
            );
        }


        if (request.getOnwardFlightId() == null) {

            throw new RuntimeException(
                    "Departure flight is required"
            );
        }


        if (
                request.getPassengers() == null ||
                        request.getPassengers().isEmpty()
        ) {

            throw new RuntimeException(
                    "At least one passenger is required"
            );
        }


        // ========================================================
        // GET DEPARTURE FLIGHT
        // ========================================================

        Flight onwardFlight =
                flightRepository
                        .findById(
                                request.getOnwardFlightId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Departure flight not found"
                                )
                        );


        // ========================================================
        // GET RETURN FLIGHT
        // ========================================================

        Flight returnFlight = null;

        if (request.getReturnFlightId() != null) {

            returnFlight =
                    flightRepository
                            .findById(
                                    request.getReturnFlightId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Return flight not found"
                                    )
                            );
        }


        // ========================================================
        // PASSENGER COUNT
        // ========================================================

        int passengerCount =
                request.getPassengers().size();


        // ========================================================
        // CHECK DEPARTURE SEATS
        // ========================================================

        if (
                onwardFlight.getAvailableSeats() <
                        passengerCount
        ) {

            throw new RuntimeException(
                    "Not enough seats available on departure flight"
            );
        }


        // ========================================================
        // CHECK RETURN SEATS
        // ========================================================

        if (
                returnFlight != null &&
                        returnFlight.getAvailableSeats() <
                                passengerCount
        ) {

            throw new RuntimeException(
                    "Not enough seats available on return flight"
            );
        }


        // ========================================================
        // CALCULATE PRICE
        // ========================================================

        BigDecimal onwardPrice =
                onwardFlight.getPrice() != null
                        ? onwardFlight.getPrice()
                        : BigDecimal.ZERO;


        BigDecimal returnPrice =
                returnFlight != null &&
                        returnFlight.getPrice() != null
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


        // ========================================================
        // GENERATE BOOKING REFERENCE / PNR
        // ========================================================

        String bookingReference =
                generateBookingReference();


        // ========================================================
        // CREATE BOOKING
        // ========================================================

        Booking booking =
                new Booking();


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


        // ========================================================
        // SAVE BOOKING
        // ========================================================

        booking =
                bookingRepository.save(
                        booking
                );


        // ========================================================
        // SAVE PASSENGERS
        // ========================================================

        Passenger primaryPassenger = null;


        for (
                PassengerRequest passengerRequest :
                request.getPassengers()
        ) {

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


            passenger.setEmail(
                    passengerRequest.getEmail()
            );


            passenger.setPhone(
                    passengerRequest.getPhone()
            );


            passenger.setDateOfBirth(
                    passengerRequest.getDateOfBirth()
            );


            passenger =
                    passengerRepository.save(
                            passenger
                    );


            /*
             * First passenger is treated as
             * primary passenger for notification.
             */

            if (primaryPassenger == null) {

                primaryPassenger = passenger;
            }
        }


        // ========================================================
        // REDUCE DEPARTURE SEATS
        // ========================================================

        onwardFlight.setAvailableSeats(
                onwardFlight.getAvailableSeats()
                        - passengerCount
        );


        flightRepository.save(
                onwardFlight
        );


        // ========================================================
        // REDUCE RETURN SEATS
        // ========================================================

        if (returnFlight != null) {

            returnFlight.setAvailableSeats(
                    returnFlight.getAvailableSeats()
                            - passengerCount
            );


            flightRepository.save(
                    returnFlight
            );
        }


        // ========================================================
        // CREATE NOTIFICATION DTO
        // ========================================================

        BookingConfirmationDto confirmationDto =
                convertToConfirmationDto(
                        booking,
                        primaryPassenger,
                        request
                );


        // ========================================================
        // SEND EMAIL
        // ========================================================

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


        // ========================================================
        // RESPONSE
        // ========================================================

        return new CreateBookingResponse(

                booking.getId(),

                booking.getBookingReference(),

                booking.getStatus(),

                "Booking created successfully"
        );
    }


    // ============================================================
    // CONVERT BOOKING → CONFIRMATION DTO
    // ============================================================

    private BookingConfirmationDto convertToConfirmationDto(
            Booking booking,
            Passenger passenger,
            CreateBookingRequest request) {

        Flight flight =
                booking.getOnwardFlight();


        String passengerName =
                passenger.getFirstName()
                        + " "
                        + passenger.getLastName();


        return BookingConfirmationDto.builder()

                // Booking information
                .bookingId(
                        booking.getId()
                )

                .bookingReference(
                        booking.getBookingReference()
                )


                // Passenger information
                .passengerName(
                        passengerName
                )

                .email(
                        passenger.getEmail()
                )

                .phone(
                        passenger.getPhone()
                )


                // Flight information
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


                // Booking details
                .onwardSeatNumber(
                        request.getOnwardSeatNumber()
                )

                .returnSeatNumber(
                        request.getReturnSeatNumber()
                )

                .cabin(
                        flight.getCabin() != null
                                ? flight.getCabin().toString()
                                : null
                )

                .totalAmount(
                        booking.getTotalAmount()
                )

                .bookingStatus(
                        booking.getStatus()
                )

                .build();
    }


    // ============================================================
    // GENERATE BOOKING REFERENCE / PNR
    // ============================================================

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