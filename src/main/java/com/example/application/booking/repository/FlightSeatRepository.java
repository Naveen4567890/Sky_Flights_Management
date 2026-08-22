package com.example.application.booking.repository;

import com.example.application.booking.entity.FlightSeat;
import com.example.application.flight.entity.Flight;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FlightSeatRepository
        extends JpaRepository<FlightSeat, Long> {

    Optional<FlightSeat> findByFlightAndSeatNumber(
            Flight flight,
            String seatNumber
    );

    List<FlightSeat> findByFlight(Flight flight);

    List<FlightSeat> findByFlightAndStatus(
            Flight flight,
            String status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT fs
            FROM FlightSeat fs
            WHERE fs.flight = :flight
            AND fs.seatNumber = :seatNumber
            """)
    Optional<FlightSeat> findSeatForUpdate(
            @Param("flight") Flight flight,
            @Param("seatNumber") String seatNumber
    );
}