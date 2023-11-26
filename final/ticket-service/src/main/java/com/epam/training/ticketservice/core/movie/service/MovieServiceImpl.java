package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {


    private final MovieRepository movieRepository;

    @Override
    public void createMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(String title, String genre, int length) {
        Movie movie = movieRepository.findByTitle(title).get();
        movie.setGenre(genre);
        movie.setLength(length);
        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void deleteMovie(String title) {
        movieRepository.deleteByTitle(title);
    }

    @Override
    public List<MovieDto> listMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> new MovieDto(movie.getTitle(),movie.getGenre(),movie.getLength()))
                .toList();
    }

    @Override
    public Optional<MovieDto> findMovie(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        return movie.map(value -> new MovieDto(value.getTitle(), value.getGenre(), value.getLength()));
    }

}
