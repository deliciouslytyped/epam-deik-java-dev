package com.epam.training.ticketservice.core.user;

import java.util.Optional;

public interface UserService {

    Optional<User> signin(String username, String password);

    Optional<User> signout();

    Optional<User> describe();

    void registerUser(String username,String password);
}
