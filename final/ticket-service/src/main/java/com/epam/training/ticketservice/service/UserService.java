package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.UserDto;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;
import org.springframework.security.core.Authentication;

public interface UserService {

    Result<UserDto, OperationException> getUser(Authentication auth);

    Result<?, OperationException> createUser(String username, String password);
}
