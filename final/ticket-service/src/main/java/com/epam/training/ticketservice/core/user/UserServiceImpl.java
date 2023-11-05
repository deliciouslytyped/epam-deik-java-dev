package com.epam.training.ticketservice.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private User signedInUser = null;

    @Override
    public Optional<User> signInPrivileged(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        signedInUser = new User(username,password,Role.ADMIN);
        return describe();
    }

    @Override
    public Optional<User> signIn(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        signedInUser = new User(username,password,Role.USER);
        return describe();
    }

    @Override
    public Optional<User> signout() {
        Optional<User> prevSignedInUser = describe();
        signedInUser = null;
        return prevSignedInUser;
    }

    @Override
    public Optional<User> describe() {
        return Optional.ofNullable(signedInUser);
    }

    @Override
    public void signUp(String username, String password) {
        userRepository.save(new User(username,password,Role.USER));
    }


}
