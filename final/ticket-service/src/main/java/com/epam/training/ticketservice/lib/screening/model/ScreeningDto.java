package com.epam.training.ticketservice.lib.screening.model;

import lombok.Data;

import java.time.Instant;

@Data
public class ScreeningDto {
    private final String movieTitle;
    private final String roomName;
    private final Instant time;
}
