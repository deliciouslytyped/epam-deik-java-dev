package com.epam.training.ticketservice.core.movie.model;

import com.epam.training.ticketservice.core.movie.persistence.Movie;

public record MovieDto(String title, String genre, int length){

    public MovieDto(Movie movie) {
        this(movie.getTitle(), movie.getGenre(), movie.getLength());
    }
}
