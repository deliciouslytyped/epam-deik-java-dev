package com.epam.training.ticketservice.core.movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    void createMovie(String title, String genre, int length);

    void updateMovie(String title, String genre, int length);

    void deleteMovie(String title);

    List<Movie> listMovies();

    Optional<Movie> findMovie(String title);
}
