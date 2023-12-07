package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomRepository;
import com.epam.training.ticketservice.lib.util.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.lib.util.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO can we somehow genericize this CRUD? The problem is that if we want the service layer to hide the database layer,
// we have to take the constructor parameters as method parameters, which means we cant use a common iterface method signature.

//TODO spring / hibernate logs constraint violations as visible errors
/**
 * @see Room
 */
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    protected final RoomRepository repo;
    /*
    @PersistenceContext
    protected final EntityManager em;
    */
    //TODO an alternative approach is to return a sum type and/or use checked exceptions which is basically the same thing.
    @Override
    public void create(@NonNull String name, @NonNull Integer rowCount, @NonNull Integer colCount) {
        new ConstraintViolationHandler(() -> { //Not using simple try catch for this because hibernate makes it a pain to access the violation type? so im trying to clean up the business logic like this?
            repo.save(new Room(name, rowCount, colCount));
        }).on(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, ()-> {
            throw new AlreadyExistsException("Room"); //TODO this results in a string on the terminal, why?
        }).on(List.of("CHECK_ROW_COUNT", "CHECK_COL_COUNT"), (cname) -> { switch(cname) {
            case "CHECK_ROW_COUNT" -> throw new ApplicationDomainException("The number of rows in a room should be positive."); //TODO well-typed "return"s
            case "CHECK_COL_COUNT" -> throw new ApplicationDomainException("The number of columns in a room should be positive.");
        };}).run();
    }

    //TODO if not exist?
    @Override
    public void update(@NonNull String name, @NonNull Integer rowCount, @NonNull Integer colCount) {
        new ConstraintViolationHandler(() -> {
            repo.updateRowCountAndColCountByName(rowCount, colCount, name);
        }).on(List.of("CHECK_ROW", "CHECK_COL"), (cname) -> { switch (cname) {
            case "CHECK_ROW" -> throw new ApplicationDomainException("Row number falls out of bounds.");
            case "CHECK_COL" -> throw new ApplicationDomainException("Column number falls out of bounds.");
        };}).run();
    }

    @Override
    public void delete(@NonNull String name) {
        repo.deleteById(name); //TODO what happens if empty?
    }

    @Override
    public List<RoomDto> list() {
        return repo.findAll().stream().map(RoomDto::new).toList();
    }
}
