package com.example.application.flight.controller;

import com.example.application.flight.dto.SeatUpdate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SeatWebSocketController {

    @MessageMapping("/seat")
    @SendTo("/topic/seats")
    public SeatUpdate updateSeat(SeatUpdate update) {

        System.out.println(
                "Seat updated: " +
                        update.getSeatNumber()
        );

        return update;
    }
}