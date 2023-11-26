package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.persistence.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    void createMovie(Movie movie);

    void updateMovie(String title, String genre, int length);

    void deleteMovie(String title);

    List<Movie> listMovies();

    Optional<Movie> findMovie(String title);
}
