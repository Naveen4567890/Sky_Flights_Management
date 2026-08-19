package com.example.application.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId;

    private String paymentId;

    private String signature;

    @Column(nullable = false)
    private BigDecimal amount;

    private String currency;

    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;


    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


    public enum PaymentStatus {
        CREATED,
        SUCCESS,
        FAILED
    }



}
