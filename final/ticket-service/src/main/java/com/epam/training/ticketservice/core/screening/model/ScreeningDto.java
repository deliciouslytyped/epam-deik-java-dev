package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.Screening;

public record ScreeningDto(String movieName, String roomName, String date) {

    private static RoomRepository roomRepository;
    private static MovieRepository movieRepository;

    public ScreeningDto(Screening screening) {
        this(screening.getMovieName(), screening.getRoomName(),screening.getFormattedDate());
    }
}
