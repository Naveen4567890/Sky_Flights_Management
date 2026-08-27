package com.example.application.booking.controller;

import com.example.application.booking.dto.CreateBookingRequest;
import com.example.application.booking.dto.CreateBookingResponse;
import com.example.application.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Tag(
        name = "Booking Management",
        description = "APIs for creating and managing flight bookings"
)
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Create a flight booking",
            description = """
                    Creates a new flight booking using the selected
                    flight, passenger details, seat information and
                    payment details.
                    """
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "201",
                    description = "Booking created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreateBookingResponse.class
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid booking request"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Flight or passenger information not found"
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Flight seat is no longer available"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<CreateBookingResponse> createBooking(
            @RequestBody CreateBookingRequest request
    ) {

        CreateBookingResponse response =
                bookingService.createBooking(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}