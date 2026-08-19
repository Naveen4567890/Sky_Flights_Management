package com.example.application.payment.controller;

import com.example.application.payment.dto.CreatePaymentRequest;
import com.example.application.payment.dto.CreatePaymentResponse;
import com.example.application.payment.dto.VerifyPaymentRequest;
import com.example.application.payment.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }


    // ==========================================
    // CREATE ORDER
    // ==========================================

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestBody
            CreatePaymentRequest request
    ) {

        try {

            CreatePaymentResponse response =
                    paymentService.createOrder(
                            request
                    );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            e.getMessage()
                    );
        }
    }


    // ==========================================
    // VERIFY PAYMENT
    // ==========================================

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody
            VerifyPaymentRequest request
    ) {

        try {

            boolean verified =
                    paymentService.verifyPayment(
                            request
                    );


            if (!verified) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Payment verification failed"
                        );
            }


            return ResponseEntity.ok(
                    "Payment verified successfully"
            );


        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            e.getMessage()
                    );
        }
    }
}