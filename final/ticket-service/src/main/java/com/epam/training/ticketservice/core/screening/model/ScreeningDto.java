package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.Screening;

public record ScreeningDto(MovieDto movie, RoomDto room, String date) {

    private static final RoomRepository roomRepository = null;
    private static final MovieRepository movieRepository = null;

    public ScreeningDto(Screening screening) {
        this(new MovieDto(movieRepository.findByTitle(screening.getMovieName()).get()),
                new RoomDto(roomRepository.findByName(screening.getRoomName()).get()),
                screening.getDate().toString());
    }
}
