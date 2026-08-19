package com.example.application.booking.controller;

import com.example.application.booking.dto.CreateBookingRequest;
import com.example.application.booking.dto.CreateBookingResponse;
import com.example.application.booking.service.BookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService
    ) {
        this.bookingService = bookingService;
    }


    // ==========================================
    // CREATE BOOKING
    // ==========================================

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(
            @RequestBody CreateBookingRequest request
    ) {

        try {

            CreateBookingResponse response =
                    bookingService.createBooking(request);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}