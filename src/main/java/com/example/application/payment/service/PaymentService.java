package com.example.application.payment.service;

import com.example.application.flight.notification.NotificationMessage;
import com.example.application.flight.notification.NotificationService;

import com.example.application.payment.dto.CreatePaymentRequest;
import com.example.application.payment.dto.CreatePaymentResponse;
import com.example.application.payment.dto.VerifyPaymentRequest;
import com.example.application.payment.entity.Payment;
import com.example.application.payment.repository.PaymentRepository;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final NotificationService notificationService;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;


    public PaymentService(
            PaymentRepository paymentRepository,
            NotificationService notificationService
    ) {
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
    }


    // ==========================================
    // CREATE PAYMENT ORDER
    // ==========================================

    public CreatePaymentResponse createOrder(
            CreatePaymentRequest request
    ) throws Exception {

        if (request.amount() == null ||
                request.amount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Invalid payment amount"
            );
        }


        RazorpayClient razorpayClient =
                new RazorpayClient(
                        keyId,
                        keySecret
                );


        // Razorpay accepts amount in paise
        long amountInPaise =
                request.amount()
                        .multiply(BigDecimal.valueOf(100))
                        .longValue();


        JSONObject orderRequest =
                new JSONObject();

        orderRequest.put(
                "amount",
                amountInPaise
        );

        orderRequest.put(
                "currency",
                "INR"
        );

        orderRequest.put(
                "receipt",
                "flight_" +
                        System.currentTimeMillis()
        );


        Order order =
                razorpayClient.orders.create(
                        orderRequest
                );


        String orderId =
                order.get("id");


        Payment payment =
                new Payment();

        payment.setOrderId(orderId);

        payment.setAmount(
                request.amount()
        );

        payment.setCurrency("INR");

        payment.setPaymentMethod(
                request.paymentMethod()
        );

        payment.setStatus(
                Payment.PaymentStatus.CREATED
        );


        paymentRepository.save(payment);


        return new CreatePaymentResponse(
                orderId,
                keyId,
                request.amount(),
                "INR"
        );
    }


    // ==========================================
    // VERIFY PAYMENT
    // ==========================================

    public boolean verifyPayment(
            VerifyPaymentRequest request
    ) throws Exception {

        Payment payment =
                paymentRepository
                        .findByOrderId(
                                request.orderId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Payment order not found"
                                )
                        );


        String data =
                request.orderId()
                        + "|"
                        + request.paymentId();


        String generatedSignature =
                generateSignature(
                        data,
                        keySecret
                );


        boolean valid =
                generatedSignature.equals(
                        request.signature()
                );


        // ==========================================
        // PAYMENT FAILED
        // ==========================================

        if (!valid) {

            payment.setStatus(
                    Payment.PaymentStatus.FAILED
            );

            paymentRepository.save(payment);


            NotificationMessage notification =
                    new NotificationMessage(
                            "PAYMENT_FAILED",
                            "Payment failed. Please try again.",
                            "FAILED"
                    );


            notificationService.sendPaymentNotification(
                    notification
            );


            return false;
        }


        // ==========================================
        // PAYMENT SUCCESS
        // ==========================================

        payment.setPaymentId(
                request.paymentId()
        );

        payment.setSignature(
                request.signature()
        );

        payment.setStatus(
                Payment.PaymentStatus.SUCCESS
        );

        payment.setPaidAt(
                LocalDateTime.now()
        );


        paymentRepository.save(payment);


        // ==========================================
        // WEBSOCKET NOTIFICATION
        // ==========================================

        NotificationMessage notification =
                new NotificationMessage(
                        "PAYMENT_SUCCESS",
                        "Payment successful. Your booking is confirmed.",
                        "SUCCESS"
                );


        notificationService.sendPaymentNotification(
                notification
        );


        return true;
    }


    // ==========================================
    // SIGNATURE GENERATION
    // ==========================================

    private String generateSignature(
            String data,
            String secret
    ) throws Exception {

        Mac mac =
                Mac.getInstance("HmacSHA256");


        SecretKeySpec secretKey =
                new SecretKeySpec(
                        secret.getBytes(
                                StandardCharsets.UTF_8
                        ),
                        "HmacSHA256"
                );


        mac.init(secretKey);


        byte[] hash =
                mac.doFinal(
                        data.getBytes(
                                StandardCharsets.UTF_8
                        )
                );


        StringBuilder hex =
                new StringBuilder();


        for (byte b : hash) {

            hex.append(
                    String.format(
                            "%02x",
                            b
                    )
            );
        }


        return hex.toString();
    }
}