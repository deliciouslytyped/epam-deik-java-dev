package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;

    @Override
    public @NotNull Result<?, OperationException> createMovie(String title, String category, int length) {
        if (repository.existsByTitleIgnoreCase(title)) {
            return Result.err(new AlreadyExistsException("Movie"));
        }
        repository.save(new Movie(title, category, length));
        return Result.ok(null);
    }

    @Override
    public @NotNull Result<?, OperationException> updateMovie(String title, String category, int length) {
        var movie = repository.findByTitle(title);
        if (movie.isEmpty()) {
            return Result.err(new NotFoundException("Movie"));
        }
        movie.get().setCategory(category);
        movie.get().setLength(length);
        repository.save(movie.get());
        return Result.ok(null);
    }

    @Override
    public @NotNull Result<?, OperationException> deleteMovie(String title) {
        if (!repository.existsByTitleIgnoreCase(title)) {
            return Result.err(new NotFoundException("Movie"));
        }
        repository.deleteByTitle(title);
        return Result.ok(null);
    }

    @Override
    public @NotNull Result<List<MovieDto>, OperationException> listMovies() {
        return Result.ok(repository.findAll().stream().map(MovieDto::new).toList());
    }
}
