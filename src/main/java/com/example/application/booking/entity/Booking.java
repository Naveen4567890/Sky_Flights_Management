package com.example.application.booking.entity;

import com.example.application.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "onward_flight_id",
            nullable = false
    )
    private Flight onwardFlight;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "return_flight_id"
    )
    private Flight returnFlight;


    @Column(nullable = false)
    private Integer passengerCount;


    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal totalAmount;


    private String paymentMethod;

    private String paymentId;

    @Column(nullable = false)
    private String status;

    // Booking creation date/time
    @Column(nullable = false)
    private LocalDateTime bookingDate;

    // Passengers belonging to this booking
    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Passenger> passengers = new ArrayList<>();

    // Add passenger
    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setBooking(this);
    }

    // Remove passenger
    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        passenger.setBooking(null);
    }
}