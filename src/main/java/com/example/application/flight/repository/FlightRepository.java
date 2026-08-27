package com.example.application.flight.repository;

import com.example.application.flight.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCaseAndTravelDateAndCabin(
            String source,
            String destination,
            LocalDate travelDate,
            String cabin
    );

  /*@Query("""
        SELECT f FROM Flight f
        WHERE LOWER(f.source) = LOWER(:source)
        AND LOWER(f.destination) = LOWER(:destination)
        AND f.travelDate = :travelDate
        AND f.cabin = :cabin
        AND (:airline IS NULL OR LOWER(f.airline) = LOWER(:airline))
        AND (:stops IS NULL OR f.stops = :stops)
        AND (:minPrice IS NULL OR f.price >= :minPrice)
        AND (:maxPrice IS NULL OR f.price <= :maxPrice)
    """)
  List<Flight> searchFlights(
          String source,
          String destination,
          LocalDate travelDate,
          String cabin,
          String airline,
          Integer stops,
          BigDecimal minPrice,
          BigDecimal maxPrice
  );*/

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByAirlineIgnoreCase(String airline);
}
