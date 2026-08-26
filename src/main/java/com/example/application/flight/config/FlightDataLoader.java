package com.example.application.flight.config;

import com.example.application.flight.data.AirportData;
import com.example.application.flight.entity.Flight;
import com.example.application.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class FlightDataLoader implements CommandLineRunner {

    private final FlightRepository flightRepository;

    private final Random random = new Random();

    private static final String[] AIRLINES = {
            "IndiGo",
            "Air India",
            "Air India Express",
            "Akasa Air",
            "Vistara",
            "SpiceJet",
            "Emirates",
            "Qatar Airways",
            "Singapore Airlines",
            "Etihad Airways",
            "Lufthansa",
            "British Airways",
            "Thai Airways",
            "Malaysia Airlines",
            "SriLankan Airlines"
    };

    private static final String[] CABINS = {
            "Economy",
            "Premium Economy",
            "Business"
    };

    @Override
    public void run(String... args) {

        // Prevent duplicate data whenever application restarts
        if (flightRepository.count() > 0) {
            System.out.println("Flight data already exists. Skipping data loading.");
            return;
        }

        System.out.println("Generating flight data...");

        List<Flight> flights = new ArrayList<>();

        /*
         * Generate 600 flights
         */
        for (int i = 0; i < 600; i++) {

            Flight flight = generateFlight();

            flights.add(flight);
        }

        flightRepository.saveAll(flights);

        System.out.println(
                "Successfully inserted " + flights.size() + " flights."
        );
    }

    private Flight generateFlight() {

        AirportData.Airport source;
        AirportData.Airport destination;

        /*
         * Make sure source and destination are different
         */
        do {
            source = getRandomAirport();
            destination = getRandomAirport();
        } while (source.code().equals(destination.code()));

        String airline = getRandomAirline();

        String flightNumber = generateFlightNumber(airline);

        LocalDate travelDate = generateTravelDate();

        LocalDateTime departureTime =
                generateDepartureTime(travelDate);

        int stops = generateStops();

        long durationMinutes =
                generateDuration(source, destination, stops);

        LocalDateTime arrivalTime =
                departureTime.plusMinutes(durationMinutes);

        String duration =
                formatDuration(durationMinutes);

        String cabin = getRandomCabin();

        Integer availableSeats =
                generateAvailableSeats(cabin);

        BigDecimal price =
                generatePrice(source, destination, cabin, stops);

        Flight flight = new Flight();

        flight.setAirline(airline);
        flight.setFlightNumber(flightNumber);

        /*
         * Store airport codes.
         *
         * Example:
         * MAA -> DEL
         */
        flight.setSource(source.code());
        flight.setDestination(destination.code());

        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setTravelDate(travelDate);

        flight.setDuration(duration);
        flight.setStops(stops);
        flight.setCabin(cabin);
        flight.setAvailableSeats(availableSeats);
        flight.setPrice(price);

        return flight;
    }

    private AirportData.Airport getRandomAirport() {

        return AirportData.AIRPORTS.get(
                random.nextInt(AirportData.AIRPORTS.size())
        );
    }

    private String getRandomAirline() {

        return AIRLINES[
                random.nextInt(AIRLINES.length)
                ];
    }

    private String generateFlightNumber(String airline) {

        String prefix;

        switch (airline) {

            case "IndiGo" -> prefix = "6E";

            case "Air India" -> prefix = "AI";

            case "Air India Express" -> prefix = "IX";

            case "Akasa Air" -> prefix = "QP";

            case "Vistara" -> prefix = "UK";

            case "SpiceJet" -> prefix = "SG";

            case "Emirates" -> prefix = "EK";

            case "Qatar Airways" -> prefix = "QR";

            case "Singapore Airlines" -> prefix = "SQ";

            case "Etihad Airways" -> prefix = "EY";

            case "Lufthansa" -> prefix = "LH";

            case "British Airways" -> prefix = "BA";

            case "Thai Airways" -> prefix = "TG";

            case "Malaysia Airlines" -> prefix = "MH";

            case "SriLankan Airlines" -> prefix = "UL";

            default -> prefix = "FL";
        }

        int number = 100 + random.nextInt(900);

        return prefix + "-" + number;
    }

    private LocalDate generateTravelDate() {

        /*
         * Generate flights between today
         * and the next 60 days.
         */
        return LocalDate.now()
                .plusDays(random.nextInt(61));
    }

    private LocalDateTime generateDepartureTime(
            LocalDate travelDate) {

        /*
         * Departure between 05:00 and 23:00
         */

        int hour = 5 + random.nextInt(18);

        int minute =
                random.nextInt(4) * 15;

        return travelDate.atTime(hour, minute);
    }

    private int generateStops() {

        /*
         * Mostly direct flights.
         */

        int value = random.nextInt(100);

        if (value < 65) {
            return 0;
        }

        if (value < 90) {
            return 1;
        }

        return 2;
    }

    private long generateDuration(
            AirportData.Airport source,
            AirportData.Airport destination,
            int stops) {

        /*
         * Domestic flights:
         * 1 - 4 hours
         *
         * International:
         * 3 - 16 hours
         */

        boolean sourceIndia =
                source.country().equals("India");

        boolean destinationIndia =
                destination.country().equals("India");

        int minimum;
        int maximum;

        if (sourceIndia && destinationIndia) {

            minimum = 60;
            maximum = 240;

        } else if (!sourceIndia && !destinationIndia) {

            minimum = 180;
            maximum = 960;

        } else {

            minimum = 180;
            maximum = 600;
        }

        long duration =
                minimum +
                        random.nextInt(
                                maximum - minimum + 1
                        );

        /*
         * Add approximately 60 minutes
         * for each stop.
         */
        duration += stops * 60L;

        return duration;
    }

    private String formatDuration(long minutes) {

        Duration duration =
                Duration.ofMinutes(minutes);

        long hours = duration.toHours();

        long remainingMinutes =
                duration.toMinutesPart();

        return hours + "h "
                + remainingMinutes + "m";
    }

    private String getRandomCabin() {

        int value = random.nextInt(100);

        if (value < 80) {
            return "Economy";
        }

        if (value < 95) {
            return "Premium Economy";
        }

        return "Business";
    }

    private int generateAvailableSeats(String cabin) {

        return switch (cabin) {

            case "Economy" ->
                    20 + random.nextInt(151);

            case "Premium Economy" ->
                    5 + random.nextInt(46);

            case "Business" ->
                    2 + random.nextInt(31);

            default ->
                    20 + random.nextInt(100);
        };
    }

    private BigDecimal generatePrice(
            AirportData.Airport source,
            AirportData.Airport destination,
            String cabin,
            int stops) {

        boolean domestic =
                source.country().equals("India")
                        && destination.country().equals("India");

        double price;

        if (domestic) {

            /*
             * Domestic:
             * ₹2,500 - ₹15,000
             */
            price =
                    2500 +
                            random.nextDouble() * 12;

        } else {

            /*
             * International:
             * ₹15,000 - ₹1,50,000
             */
            price =
                    15000 +
                            random.nextDouble() * 13;
        }

        /*
         * Business class is more expensive.
         */
        if (cabin.equals("Premium Economy")) {

            price *= 1.5;

        } else if (cabin.equals("Business")) {

            price *= 2.5;
        }

        /*
         * Add price for stops.
         */
        price += stops * 1000;

        /*
         * Round to nearest 100.
         */
        price =
                Math.round(price / 100) * 100;

        return BigDecimal
                .valueOf(price)
                .setScale(2);
    }
}