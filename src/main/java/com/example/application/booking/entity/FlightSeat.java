package com.example.application.booking.entity;

import com.example.application.flight.entity.Flight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "flight_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flight_seat",
                        columnNames = {
                                "flight_id",
                                "seat_number"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "flight_id",
            nullable = false
    )
    private Flight flight;

    @Column(
            name = "seat_number",
            nullable = false
    )
    private String seatNumber;

    @Column(nullable = false)
    private String status;
}
