package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Screening;
import lombok.Getter;

@Getter
public class ScreeningDTO {
    private final MovieDTO movie;
    private final String roomName;
    private final String startTime;

    public ScreeningDTO(Screening dao) {
        this.movie = new MovieDTO(dao.getMovie());
        this.roomName = dao.getRoom().getName();
        this.startTime = dao.getStartTime().format(Screening.TIME_FORMAT);
    }

    @Override
    public String toString() {
        return movie.getTitle() + " (" + movie.getCategory() + ", " + movie.getLength() + " minutes), screened in room" +
            roomName + ", at " + startTime;
    }
}
