package com.epam.training.ticketservice.core.user;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> signInPrivileged(String username, String password);

    Optional<UserDto> signIn(String username, String password);

    Optional<UserDto> signout();

    Optional<UserDto> describe();

    void signUp(String username, String password);
}
