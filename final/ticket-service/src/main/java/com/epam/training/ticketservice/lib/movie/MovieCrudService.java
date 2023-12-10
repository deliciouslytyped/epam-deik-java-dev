package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.lib.db.CustomCrudService;
import com.epam.training.ticketservice.lib.db.CustomCrudServiceImpl;
import com.epam.training.ticketservice.lib.db.constraints.ConstraintFilters;
import com.epam.training.ticketservice.lib.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.lib.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.util.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.lib.util.exceptions.ApplicationDomainException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MovieCrudService extends CustomCrudService<MovieDto, Movie, String, MovieCrudRepository> {
}


