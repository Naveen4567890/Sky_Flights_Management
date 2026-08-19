package com.example.application.flight.dto;

import lombok.*;

@Data
@Getter
@Setter

public class SeatUpdate {

    private Long flightId;
    private String seatNumber;
    private String status;


}
