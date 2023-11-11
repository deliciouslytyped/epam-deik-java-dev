package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.dto.UserDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.model.UserRole;
import com.epam.training.ticketservice.repository.UserRepository;
import com.epam.training.ticketservice.service.UserService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Result<UserDto, OperationException> getUser(Authentication auth) {
        if (auth == null) {
            return Result.err(new NotFoundException("User"));
        }
        var user = repository.findByUsername(auth.getName());
        return user.<Result<UserDto, OperationException>>map(value -> Result.ok(new UserDto(value)))
                .orElseGet(() -> Result.err(new NotFoundException("User")));
    }

    @Override
    public Result<?, OperationException> createUser(String username, String password) {
        if (repository.existsByUsername(username)) {
            return Result.err(new AlreadyExistsException("User"));
        }
        repository.save(new User(username, password, UserRole.USER));
        return Result.ok(null);
    }
}
