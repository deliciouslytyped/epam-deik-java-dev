package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.lib.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.util.exceptions.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO can we somehow genericize this CRUD? The problem is that if we want the service layer to hide the database layer,
// we have to take the constructor parameters as method parameters, which means we cant use a common iterface method signature.

//TODO spring / hibernate logs constraint violations as visible errors
/**
 * @see Room
 */
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    protected final MovieRepository repo;
    /*
    @PersistenceContext
    protected final EntityManager em;
    */
    //TODO an alternative approach is to return a sum type and/or use checked exceptions which is basically the same thing.
    @Override
    public void create(@NonNull String title, @NonNull String genre, @NonNull int runtime) {
        new ConstraintViolationHandler(() -> { //Not using simple try catch for this because hibernate makes it a pain to access the violation type? so im trying to clean up the business logic like this?
            repo.save(new Movie(title, genre, runtime));
        }).on(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, ()-> {
            throw new AlreadyExistsException("Movie");
        }).on("CHECK_RUN_TIME", () -> {
            //TODO
        }).run();
    }

    //TODO if not exist?
    @Override
    public void update(@NonNull String title, @NonNull String genre, @NonNull int runtime) {
        new ConstraintViolationHandler(() -> {
            repo.updateGenreAndRuntimeByTitle(genre, runtime, title);
        }).on("CHECK_RUN_TIME", () -> {
//TODO
        }).run();
    }

    @Override
    public void delete(@NonNull String title) {
        repo.deleteById(title); //TODO what happens if empty?
    }

    @Override
    public List<MovieDto> list() {
        return repo.findAll().stream().map(MovieDto::new).toList();
    }

    @Override
    public Optional<MovieDto> get(String title) {
        return repo.findById(title).map(MovieDto::new);
    }

}
