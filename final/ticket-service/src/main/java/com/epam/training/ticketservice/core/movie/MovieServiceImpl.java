package com.epam.training.ticketservice.core.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {


    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int length) {
        movieRepository.save(new Movie(title,genre,length));
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

}
