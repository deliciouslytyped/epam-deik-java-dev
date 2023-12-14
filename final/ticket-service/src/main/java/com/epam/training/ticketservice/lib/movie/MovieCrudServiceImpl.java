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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieCrudServiceImpl extends CustomCrudServiceImpl<MovieDto, Movie, String, MovieCrudRepository> implements MovieCrudService {
    MovieCrudServiceImpl(MovieCrudRepository repo){
        super(repo);
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
                        List.of("CHECK_RUN_TIME")); //TODO see, this interface is bad and complicated because I had to get the debugger out to realize I copy-pasted the constaraint filter and forgot to change the constraint name to filter on. This is redundant with the handler hooks branching
            }

            @Override
            public void getConstraintTypeHandler(ConstraintViolationHandler.ConstraintType t, MovieDto m) { switch (t) {
                    case PRIMARY_KEY -> throw new AlreadyExistsException("Movie");
            }}

            @Override
            public void getNamedConstraintHandler(String cname, MovieDto m) { switch (cname) {
                    case "CHECK_RUN_TIME" -> throw new ApplicationDomainException("The run time of the movie should be positive."); //TODO well-typed "return"s
            }}
        };
    }
    @Override
    protected ConstraintHandlerHolder<MovieDto> getUpdateHandler() {
        return new ConstraintHandlerHolder<MovieDto>(){

            @Override
            public ConstraintFilters getConstraintFilters() {
                return ConstraintFilters.of(null, List.of("CHECK_RUN_TIME", "CHECK_UPDATE_HAS_ROWS"));
            }

            @Override
            public void getNamedConstraintHandler(String cname, MovieDto arg) { switch(cname) { //TODO uh thigh might need to be renamed
                    case "CHECK_RUN_TIME" -> throw new ApplicationDomainException("The movie run time needs to be a positive number.");
                    case "CHECK_UPDATE_HAS_ROWS" -> throw new ApplicationDomainException("The movie " + arg.getTitle() + " does not exist.");
            }}
        };
    }

    @Override
    public void delete(@NonNull String s) {
        try {
            super.delete(s);
        } catch (EmptyResultDataAccessException e) {
            throw new ApplicationDomainException("The movie " + s + " does not exist.", e);
        }
    }

    /* we dont get the exception for trying to delete a missing item becase hibernate does a lookup first and its too difficult to override, so jst use the exception hibernate returns
    @Override
    protected ConstraintHandlerHolder<MovieDto> getDeleteHandler() {
        return new ConstraintHandlerHolder<MovieDto>() {
            @Override
            public ConstraintFilters getConstraintFilters() {
                return super.getConstraintFilters();
            }

            @Override
            public void getConstraintTypeHandler(ConstraintViolationHandler.ConstraintType t, MovieDto arg) {

            }

        };
    }*/
}


