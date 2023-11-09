package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MovieService {

    @NotNull
    Result<?, OperationException> createMovie(String title, String category, int length);

    @NotNull
    Result<?, OperationException> updateMovie(String title, String category, int length);

    @NotNull
    Result<?, OperationException> deleteMovie(String title);

    @NotNull
    Result<List<MovieDto>, OperationException> listMovies();
}
