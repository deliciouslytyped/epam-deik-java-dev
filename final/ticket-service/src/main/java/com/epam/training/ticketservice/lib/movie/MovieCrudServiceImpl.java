package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.model.MovieMapper;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

@Service
public class MovieCrudServiceImpl extends CustomCrudServiceImpl<MovieDto, Movie, String, MovieMapper, MovieCrudRepository> implements MovieCrudService {
    MovieCrudServiceImpl(MovieCrudRepository repo, MovieMapper mapper){
        super(repo, mapper);
    }

    @Override
    protected ConstraintHandlerHolder<MovieDto> getCreateHandler() {
        return createConstraintHandler(
            Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwExists),
            Map.of("CHECK_RUN_TIME", Exceptions::throwPositiveRuntime));
    }
    @Override
    protected ConstraintHandlerHolder<MovieDto> getUpdateHandler() {
        return createConstraintHandler(
            null,
            Map.of(
                "CHECK_RUN_TIME", Exceptions::throwPositiveRuntime,
                "CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist
            ));
    }

    @Override
    public void delete(@NonNull String s) { //TODO see base class
        super.rawDelete(s, Exceptions::throwNotExistWithContext);
    }
}


