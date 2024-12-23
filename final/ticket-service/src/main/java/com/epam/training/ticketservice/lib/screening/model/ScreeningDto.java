package com.epam.training.ticketservice.lib.screening.model;

import lombok.Data;

import java.time.Instant;

@Data
public class ScreeningDto {
    private final Long id; //TODO hack: needed because the mapper creates a new entity when trying to save a ticker...? xref https://claude.ai/chat/378b6f56-922b-44be-8d37-e0dacd75966f
    private final String movieTitle;
    private final String roomName;
    private final Instant time;
}
