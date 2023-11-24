package com.epam.training.ticketservice.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private UserDto signedInUser = null;

    @Override
    public Optional<UserDto> signInPrivileged(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        signedInUser = new UserDto(username, Role.ADMIN);
        return describe();
    }

    @Override
    public Optional<UserDto> signIn(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username,password);

        if (user.isEmpty()) {
            return Optional.empty();
        }
        signedInUser = new UserDto(user.get().getUsername(),user.get().getRole());
        return describe();
    }

    @Override
    public Optional<UserDto> signout() {
        Optional<UserDto> prevSignedInUser = describe();
        signedInUser = null;
        return prevSignedInUser;
    }

    @Override
    public Optional<UserDto> describe() {
        return Optional.ofNullable(signedInUser);
    }

    @Override
    public void signUp(String username, String password) {
        userRepository.save(new User(username,password,Role.USER));
    }


}
