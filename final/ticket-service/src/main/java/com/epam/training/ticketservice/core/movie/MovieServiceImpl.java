package com.epam.training.ticketservice.core.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int length) {
        movieRepository.save(new Movie(title,genre,length));
    }

    @Override
    public void updateMovie(String title, String genre, int length) {
        Movie movie = movieRepository.findByName(title).get();
        movie.setGenre(genre);
        movie.setLength(length);
        movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> deleteMovie(String title) {
        return Optional.empty();
    }

    @Override
    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }

}
