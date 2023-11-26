package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Screening;
import lombok.Getter;

@Getter
public class ScreeningDto {
    private final MovieDto movie;
    private final String roomName;
    private final String startTime;

    public ScreeningDto(Screening dao) {
        this.movie = new MovieDto(dao.getMovie());
        this.roomName = dao.getRoom().getName();
        this.startTime = dao.getStartTime().format(Screening.TIME_FORMAT);
    }

    @Override
    public String toString() {
        return movie.toString() + ", screened in room " + roomName + ", at " + startTime;
    }
}
