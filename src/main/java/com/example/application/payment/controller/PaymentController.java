package com.example.application.payment.controller;

import com.example.application.payment.dto.CreatePaymentRequest;
import com.example.application.payment.dto.CreatePaymentResponse;
import com.example.application.payment.dto.VerifyPaymentRequest;
import com.example.application.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Tag(
        name = "Payment Management",
        description = "APIs for creating payment orders and verifying payments"
)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }


    // ============================================================
    // CREATE PAYMENT ORDER
    // ============================================================

    @Operation(
            summary = "Create payment order",
            description = """
                    Creates a payment order using the specified amount
                    and payment method. The returned order ID and key ID
                    are used by the frontend payment gateway integration.
                    """
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Payment order created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreatePaymentResponse.class
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid payment request"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Unable to create payment order"
            )
    })
    @PostMapping("/create-order")
    public ResponseEntity<CreatePaymentResponse> createOrder(
            @RequestBody
            CreatePaymentRequest request
    ) throws Exception {

        CreatePaymentResponse response =
                paymentService.createOrder(request);

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // VERIFY PAYMENT
    // ============================================================

    @Operation(
            summary = "Verify payment",
            description = """
                    Verifies the payment using the payment order ID,
                    payment ID and payment signature returned by the
                    payment gateway. The booking ID identifies the
                    booking associated with the payment.
                    """
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Payment verified successfully"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Payment verification failed"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Booking not found"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody
            VerifyPaymentRequest request
    ) throws Exception {

        boolean verified =
                paymentService.verifyPayment(request);

        if (!verified) {
            return ResponseEntity
                    .badRequest()
                    .body("Payment verification failed");
        }

        return ResponseEntity.ok(
                "Payment verified successfully"
        );
    }
}