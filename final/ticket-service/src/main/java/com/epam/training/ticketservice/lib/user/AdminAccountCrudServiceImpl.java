package com.epam.training.ticketservice.lib.user;

import com.epam.training.ticketservice.lib.user.model.*;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationAdmin;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationAdminCrudRepository;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

@Service
public class AdminAccountCrudServiceImpl extends CustomCrudServiceImpl<AdminDto, ApplicationAdmin, Long, AdminMapper, ApplicationAdminCrudRepository> implements AdminAccountCrudService {
    public AdminAccountCrudServiceImpl(ApplicationAdminCrudRepository repo, AdminMapper mapper) {
        super(repo, mapper);
    }

    public void register(UserCreationDto dto){
        useHandler(this::getRegisterHandler, dto, () -> { //Not using simple try catch for this because hibernate makes it a pain to access the violation type? so im trying to clean up the business logic like this?
            var dpe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            repo.save(new ApplicationAdmin(null, dto.getUsername(), dpe.encode(dto.getPassword())));
        });
    }

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
    protected ConstraintHandlerHolder<AdminDto> getCreateHandler() {
        return createConstraintHandler(
                Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwAlreadyExists),
                null);
    }

    @Override
    protected ConstraintHandlerHolder<AdminDto> getUpdateHandler() {
        return createConstraintHandler(
                null,
                Map.of("CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist)
        );
    }

    @Override
    public void delete(@NonNull Long id) { //TODO see base class
        super.rawDelete(id, Exceptions::throwNotExistWithContext);
    }
}