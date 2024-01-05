package com.epam.training.ticketservice.lib.user;

import com.epam.training.ticketservice.lib.ticket.TicketCrudService;
import com.epam.training.ticketservice.lib.ticket.TicketCrudServiceImpl;
import com.epam.training.ticketservice.lib.user.model.UserCreationDto;
import com.epam.training.ticketservice.lib.user.model.UserDto;
import com.epam.training.ticketservice.lib.user.model.UserMapper;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

@Service

public class UserAccountCrudServiceImpl extends CustomCrudServiceImpl<UserDto, ApplicationUser, Long, UserMapper, ApplicationUserCrudRepository> implements UserAccountCrudService {

    private final ApplicationUserCrudRepository applicationUserCrudRepository;

    @Autowired
    private TicketCrudServiceImpl ts;
    public UserAccountCrudServiceImpl(ApplicationUserCrudRepository repo, UserMapper mapper,
                                      ApplicationUserCrudRepository applicationUserCrudRepository) {
        super(repo, mapper);
        this.applicationUserCrudRepository = applicationUserCrudRepository;
    }

    @Override
    public void create(@NonNull UserDto entityDto) {
        throw new UnsupportedOperationException("User creation goes through the register interface because the DTO is asymmetric."); //TODO well, kinda funky that we *should* be overriding this method but using a different type prevents that.
    }

    public void register(UserCreationDto dto){
        useHandler(this::getRegisterHandler, dto, () -> { //Not using simple try catch for this because hibernate makes it a pain to access the violation type? so im trying to clean up the business logic like this?
            var dpe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            repo.save(new ApplicationUser(null, dto.getUsername(), dpe.encode(dto.getPassword()), Set.of()));
        });
    }

    //TODO interface forces public?
    public ConstraintHandlerHolder<UserCreationDto> getRegisterHandler() { // TODO  Duplicating getCreateHandler because of the type signature
        return createConstraintHandler(
                Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwAlreadyExists),
                null);
    }

    ///TODO probably shouldnt be in here?
    @SneakyThrows
    @Override
    public Long keyFromStrings(String... s) {
        return Long.parseLong(s[0]);
    }

    public int keyFromStringsSize(){
        return 1;
    }
    ///

    @Override
    protected ConstraintHandlerHolder<UserDto> getCreateHandler() {
        return createConstraintHandler(
                Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwAlreadyExists),
                null);
    }

    @Override
    protected ConstraintHandlerHolder<UserDto> getUpdateHandler() {
        return createConstraintHandler(
                null,
                Map.of("CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist)
        );
    }

    @Override
    public void delete(@NonNull Long id) { //TODO see base class
        super.rawDelete(id, Exceptions::throwNotExistWithContext);
    }

    //TODO figure out approach to handling eager values
    //TODO This stuff is ending up all over the place, I need to stufy any clean up my architecture; the mappers and services are all tangled up with the repos data designs?
    @Override
    public List<UserDto> list() {
        return repo.findAllWithBookingsAndScreenings().stream() //TODO might actually be getting most of the data I just forgot the maper might be unconfigured
                .map(mapper::entityToDto)
                .peek(dto ->
                    dto.getBookings()
                       .forEach(ticketDto -> ts.fillReservation(ticketDto, ticketDto.getTicketId())))
                .toList();
    }
}