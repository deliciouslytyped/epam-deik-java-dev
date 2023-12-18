package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.support.CustomCrudService;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;

public interface MovieCrudService extends CustomCrudService<MovieDto, String> {
}


