package com.epam.training.ticketservice.lib.movie.model;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.support.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MovieDto implements DTO<String> {
    private final String title;
    private final String genre;
    private final int runtime;

    @Override
    public String getKey() {
        return getTitle();
    }
}