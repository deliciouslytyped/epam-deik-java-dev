package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {

    Result<?, OperationException> signup(String username, String password);

    Result<?, AuthenticationException> login(String username, String password, boolean privileged);

    Result<?, AuthenticationException> logout();
}
