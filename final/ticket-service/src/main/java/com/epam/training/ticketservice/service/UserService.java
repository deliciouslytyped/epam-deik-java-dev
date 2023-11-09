package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.UserDTO;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;

public interface UserService {

    Result<UserDTO, OperationException> getUser(String username);

    Result<?, OperationException> createUser(String username, String password);
}
