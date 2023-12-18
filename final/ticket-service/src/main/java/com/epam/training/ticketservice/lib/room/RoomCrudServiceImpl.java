package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.movie.MovieCrudService;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.room.model.RoomMapper;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintFilters;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

//TODO can we somehow genericize this CRUD? The problem is that if we want the service layer to hide the database layer,
// we have to take the constructor parameters as method parameters, which means we cant use a common iterface method signature.

//TODO spring / hibernate logs constraint violations as visible errors
/**
 * @see Room
 */
@Service
public class RoomCrudServiceImpl extends CustomCrudServiceImpl<RoomDto, Room, String, RoomMapper, RoomCrudRepository> implements RoomCrudService {
    public RoomCrudServiceImpl(@NonNull RoomCrudRepository repo, @NonNull RoomMapper mapper) {
        super(repo, mapper);
    }

    @Override
    protected ConstraintHandlerHolder<RoomDto> getCreateHandler() {
        return createConstraintHandler(
            Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwAlreadyExists),
            Map.of(
                "CHECK_ROW_COUNT", Exceptions::throwPositiveRow,
                "CHECK_COL_COUNT", Exceptions::throwPositiveCol
            ));
    }

    @Override
    protected ConstraintHandlerHolder<RoomDto> getUpdateHandler() {
        return createConstraintHandler(
null,
            Map.of(
            "CHECK_ROW_COUNT", Exceptions::throwPositiveRow,
            "CHECK_COL_COUNT", Exceptions::throwPositiveCol,
            "CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist
            )
        );
    }

    @Override
    public void delete(@NonNull String s) { //TODO see base class
        super.rawDelete(s, Exceptions::throwNotExistWithContext);
    }
}
