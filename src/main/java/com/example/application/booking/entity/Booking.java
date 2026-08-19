package com.example.application.booking.entity;

import com.example.application.flight.entity.Flight;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            unique = true,
            nullable = false
    )
    private String bookingReference;


    @ManyToOne
    @JoinColumn(
            name = "onward_flight_id",
            nullable = false
    )
    private Flight onwardFlight;


    @ManyToOne
    @JoinColumn(
            name = "return_flight_id"
    )
    private Flight returnFlight;


    private Integer passengerCount;


    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal totalAmount;


    private String paymentMethod;

    private String paymentId;

    private String status;

    private LocalDateTime bookingDate;
}
