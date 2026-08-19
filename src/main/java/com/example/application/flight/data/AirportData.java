package com.example.application.flight.data;

import java.util.List;

public class AirportData {

    public record Airport(
            String city,
            String country,
            String code,
            String name
    ) {}

    public static final List<Airport> AIRPORTS = List.of(

            // India
            new Airport("Chennai", "India", "MAA", "Chennai International Airport"),
            new Airport("Delhi", "India", "DEL", "Indira Gandhi International Airport"),
            new Airport("Mumbai", "India", "BOM", "Chhatrapati Shivaji Maharaj Airport"),
            new Airport("Bangalore", "India", "BLR", "Kempegowda International Airport"),
            new Airport("Hyderabad", "India", "HYD", "Rajiv Gandhi International Airport"),
            new Airport("Kolkata", "India", "CCU", "Netaji Subhash Chandra Bose Airport"),
            new Airport("Goa", "India", "GOI", "Dabolim Airport"),
            new Airport("Goa (Mopa)", "India", "GOX", "Manohar International Airport"),
            new Airport("Kochi", "India", "COK", "Cochin International Airport"),
            new Airport("Ahmedabad", "India", "AMD", "Sardar Vallabhbhai Patel Airport"),
            new Airport("Pune", "India", "PNQ", "Pune Airport"),
            new Airport("Jaipur", "India", "JAI", "Jaipur International Airport"),
            new Airport("Lucknow", "India", "LKO", "Chaudhary Charan Singh Airport"),
            new Airport("Varanasi", "India", "VNS", "Lal Bahadur Shastri Airport"),
            new Airport("Guwahati", "India", "GAU", "Lokpriya Gopinath Bordoloi Airport"),
            new Airport("Trivandrum", "India", "TRV", "Thiruvananthapuram International Airport"),
            new Airport("Amritsar", "India", "ATQ", "Sri Guru Ram Dass Jee Airport"),
            new Airport("Coimbatore", "India", "CJB", "Coimbatore International Airport"),
            new Airport("Madurai", "India", "IXM", "Madurai Airport"),
            new Airport("Trichy", "India", "TRZ", "Tiruchirappalli International Airport"),
            new Airport("Calicut", "India", "CCJ", "Calicut International Airport"),
            new Airport("Mangalore", "India", "IXE", "Mangaluru International Airport"),

            // International
            new Airport("Dubai", "United Arab Emirates", "DXB", "Dubai International Airport"),
            new Airport("Abu Dhabi", "United Arab Emirates", "AUH", "Zayed International Airport"),
            new Airport("Doha", "Qatar", "DOH", "Hamad International Airport"),
            new Airport("Singapore", "Singapore", "SIN", "Changi Airport"),
            new Airport("Maldives", "Maldives", "MLE", "Velana International Airport"),
            new Airport("Bangkok", "Thailand", "BKK", "Suvarnabhumi Airport"),
            new Airport("Phuket", "Thailand", "HKT", "Phuket International Airport"),
            new Airport("Kuala Lumpur", "Malaysia", "KUL", "Kuala Lumpur International Airport"),
            new Airport("Bali", "Indonesia", "DPS", "Ngurah Rai International Airport"),
            new Airport("Colombo", "Sri Lanka", "CMB", "Bandaranaike International Airport"),
            new Airport("Kathmandu", "Nepal", "KTM", "Tribhuvan International Airport"),
            new Airport("London", "United Kingdom", "LHR", "London Heathrow Airport"),
            new Airport("London (Gatwick)", "United Kingdom", "LGW", "London Gatwick Airport"),
            new Airport("Paris", "France", "CDG", "Paris Charles de Gaulle Airport"),
            new Airport("Frankfurt", "Germany", "FRA", "Frankfurt Airport"),
            new Airport("Zurich", "Switzerland", "ZRH", "Zurich Airport"),
            new Airport("Amsterdam", "Netherlands", "AMS", "Amsterdam Airport Schiphol"),
            new Airport("Istanbul", "Turkey", "IST", "Istanbul Airport"),
            new Airport("Tokyo", "Japan", "HND", "Tokyo Haneda Airport"),
            new Airport("Tokyo (Narita)", "Japan", "NRT", "Narita International Airport"),
            new Airport("Hong Kong", "Hong Kong", "HKG", "Hong Kong International Airport"),
            new Airport("Sydney", "Australia", "SYD", "Sydney Kingsford Smith Airport"),
            new Airport("Melbourne", "Australia", "MEL", "Melbourne Airport"),
            new Airport("New York", "United States", "JFK", "John F. Kennedy International Airport"),
            new Airport("San Francisco", "United States", "SFO", "San Francisco International Airport"),
            new Airport("Los Angeles", "United States", "LAX", "Los Angeles International Airport"),
            new Airport("Chicago", "United States", "ORD", "O'Hare International Airport"),
            new Airport("Toronto", "Canada", "YYZ", "Toronto Pearson International Airport")
    );
}