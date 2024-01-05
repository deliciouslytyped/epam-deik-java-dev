package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.model.MovieMapper;
import com.epam.training.ticketservice.support.CustomCrudService;

//@DefaultUserPrivileged
public interface MovieCrudService extends CustomCrudService<MovieDto, String, MovieMapper> {}


