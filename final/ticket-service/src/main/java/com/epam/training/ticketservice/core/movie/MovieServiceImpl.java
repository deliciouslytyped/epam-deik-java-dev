package com.epam.training.ticketservice.core.movie;

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
    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findMovie(String title) {
        return movieRepository.findByTitle(title);
    }

}
