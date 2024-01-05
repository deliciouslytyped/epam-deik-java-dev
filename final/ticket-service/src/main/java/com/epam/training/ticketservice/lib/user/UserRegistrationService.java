package com.epam.training.ticketservice.lib.user;

import com.epam.training.ticketservice.lib.user.model.UserCreationDto;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Map;
import java.util.Set;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

public interface UserRegistrationService {
    void register(UserCreationDto dto);
    ConstraintHandlerHolder<UserCreationDto> getRegisterHandler();
}