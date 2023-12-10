package com.epam.training.ticketservice.lib.movie;

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

@Service
public class MovieCrudServiceImpl extends CustomCrudServiceImpl<MovieDto, Movie, String, MovieCrudRepository> implements MovieCrudService {
    private final MovieCrudRepository repo;

    MovieCrudServiceImpl(MovieCrudRepository repo){
        super(repo);
        this.repo = repo;
    }

    //TODO is this a layering violation?
    @Override
    protected Movie DTOtoEntity(MovieDto m) { // This is the unsafe direction
        return new Movie(m.getTitle(), m.getGenre(), m.getRuntime());
    }

    @Override
    protected MovieDto EntityToDto(Movie m) {
        return new MovieDto(m);
    }

    @Override
    protected ConstraintHandlerHolder<MovieDto> getCreateHandler() {
        return new ConstraintHandlerHolder<>() {
            @Override
            public ConstraintFilters getConstraintFilters() {
                return ConstraintFilters.of(
                        List.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY),
                        List.of("CHECK_ROW_COUNT", "CHECK_COL_COUNT"));
            }

            @Override
            public void getConstraintTypeHandler(ConstraintViolationHandler.ConstraintType t, MovieDto m) { switch (t) {
                    case PRIMARY_KEY -> throw new AlreadyExistsException("Movie");
            }}

            @Override
            public void getNamedConstraintHandler(String cname, MovieDto m) { switch (cname) {
                    case "CHECK_ROW_COUNT" -> throw new ApplicationDomainException("The number of rows in a room should be positive."); //TODO well-typed "return"s
                    case "CHECK_COL_COUNT" -> throw new ApplicationDomainException("The number of columns in a room should be positive.");
            }}
        };
    }
    @Override
    protected ConstraintHandlerHolder<MovieDto> getUpdateHandler() {
        return new ConstraintHandlerHolder<MovieDto>(){

            @Override
            public ConstraintFilters getConstraintFilters() {
                return ConstraintFilters.of(null, List.of("CHECK_RUN_TIME"));
            }

            @Override
            public void getNamedConstraintHandler(String cname, MovieDto arg) { switch(cname) {
                    case "CHECK_RUN_TIME" -> throw new ApplicationDomainException("The movie run time needs to be a positive number.");
            }}
        };
    }
}


