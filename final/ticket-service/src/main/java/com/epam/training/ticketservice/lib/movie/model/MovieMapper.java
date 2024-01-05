package com.epam.training.ticketservice.lib.movie.model;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Mapper
public abstract class MovieMapper extends CustomMapper<MovieDto, Movie, String> {
    @Autowired // TODO mapstruct doesnt work with constructor injection?
    protected MovieCrudRepository mr; // add what is needed in customcrudservice to use a service here instead?

    @SneakyThrows //TODO do the other implementations too
    @Override
    public Movie entityFromId(@NonNull String s) {
        return mr.findById(s).orElseThrow(() -> new NoSuchRecordException(s));
    }

    //TODO would be nice to have generators for these
    @Override
    public MovieDto dtoFromStrings(@NonNull String... strings) {
        return new MovieDto(strings[0], strings[1], Integer.parseInt(strings[2]));
    }

    @Override
    public MovieDto dtoFromKeyStrings(@NonNull String... strings) {
        return dtoFromStrings(strings);
    }

    @Override
    public MovieDto dtoFromJSON(@NonNull JsonNode attrs) {
        return dtoFromKeyStrings(
                Objects.requireNonNull(attrs.get("title").textValue()),
                Objects.requireNonNull(attrs.get("genre").textValue()),
                Objects.requireNonNull(attrs.get("runtime").textValue()));
    }

    @Override
    public Integer getStringsCount() {
        return 3;
    }

}
