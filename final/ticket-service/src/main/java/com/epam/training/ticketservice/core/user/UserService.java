package com.epam.training.ticketservice.core.user;

import java.util.Optional;

public interface UserService {

    Optional<User> signInPriviliged(String username, String password);

    Optional<User> signIn(String username, String password);

    Optional<User> signout();

    Optional<User> describe();

    void signUp(String username, String password);
    void registerUser(String username,String password);
}
