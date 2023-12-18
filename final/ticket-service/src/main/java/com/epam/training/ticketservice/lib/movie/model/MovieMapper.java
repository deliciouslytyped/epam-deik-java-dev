package com.epam.training.ticketservice.lib.movie.model;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.support.CustomMapper;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;

@Mapper
public interface MovieMapper extends CustomMapper<MovieDto, Movie> {
}
