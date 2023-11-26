package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.Role;
import com.epam.training.ticketservice.core.user.persistence.User;
import com.epam.training.ticketservice.core.user.persistence.UserRepository;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    private final UserService underTest = new UserServiceImpl(userRepository);

    private static User user;
    private static User admin;

    @BeforeEach
    void init() {
        user = new User("user","user",Role.USER);
        admin = new User("root","root",Role.ADMIN);
    }


    @Test
    void testSignInPrivilegedShouldSetSignedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        Optional<User> expected = Optional.of(admin);
        when(userRepository.findByUsernameAndPassword("root","root")).thenReturn(Optional.of(admin));

        //When
        Optional<UserDto> actual = underTest.signInPrivileged("root","root");

        //Then
        assertEquals(expected.get().getUsername(), actual.get().username());
        assertEquals(expected.get().getRole(), actual.get().role());
        underTest.signout();
        verify(userRepository).findByUsernameAndPassword("root","root");
    }


    @Test
    void testSignInPrivilegedShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        //Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPassword("dummy","dummy")).thenReturn(Optional.empty());

        //When
        Optional<UserDto> actual = underTest.signInPrivileged("dummy","dummy");

        //Then
        assertEquals(expected, actual);
        underTest.signout();
        verify(userRepository).findByUsernameAndPassword("dummy","dummy");
    }

    @Test
    void testSignInShouldSetSignedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));

        //When
        Optional<UserDto> actual = underTest.signIn(user.getUsername(), user.getPassword());

        //Then
        assertEquals(expected.get().getUsername(), actual.get().username());
        assertEquals(expected.get().getRole(), actual.get().role());
        underTest.signout();
        verify(userRepository).findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Test
    void testSignInShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        //Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPassword("dummy","dummy")).thenReturn(Optional.empty());

        //When
        Optional<UserDto> actual = underTest.signIn("dummy","dummy");

        //Then
        assertEquals(expected, actual);
        underTest.signout();
        verify(userRepository).findByUsernameAndPassword("dummy","dummy");
    }

    @Test
    void testSignOutShouldReturnThePreviouslySignedInUserWhenThereIsASignedInUser() {
        //Given
        when(userRepository.findByUsernameAndPassword("user","user")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.signIn("user","user");

        //When
        Optional<UserDto> actual = underTest.signout();

        //Then
        assertEquals(expected,actual);
    }

    @Test
    void testSignOutShouldReturnOptionalEmptyWhenThereIsNoOneSignedIn() {
        //Given
        Optional<UserDto> expected = Optional.empty();

        //When
        Optional<UserDto> actual = underTest.signout();

        //Then
        assertEquals(expected,actual);
    }

    @Test
    void testDescribeShouldReturnTheLoggedInUserWhenThereIsASignedInUser() {
        // Given
        when(userRepository.findByUsernameAndPassword("user", "user")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.signIn("user", "user");

        // When
        Optional<UserDto> actual = underTest.describe();

        // Then
        assertEquals(expected, actual);
        underTest.signout();
    }

    @Test
    void testDescribeShouldReturnOptionalEmptyWhenThereIsNoOneSignedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.describe();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignUpShouldSaveTheUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        when(userRepository.save(user)).thenReturn(user);

        //When
        underTest.signUp(user.getUsername(), user.getPassword());

        //Then
        verify(userRepository).save(user);
    }

    @Test
    void testDescribeShouldReturnLoggedInUserWhenAdminIsLoggedIn() {
        when(userRepository.findByUsernameAndPassword("root","root"))
                .thenReturn(Optional.of(admin));

        Optional<User> expected = Optional.of(admin);
        underTest.signInPrivileged("root","root");
        Optional<UserDto> actual = underTest.describe();

        assertEquals(expected.get().getUsername(),actual.get().username());
        assertEquals(expected.get().getRole(),actual.get().role());
        underTest.signout();

        verify(userRepository).findByUsernameAndPassword("root","root");
    }

    @Test
    void testDescribeShouldReturnLoggedInUserWhenUserIsLoggedIn() {
        when(userRepository.findByUsernameAndPassword("user","user"))
                .thenReturn(Optional.of(user));

        Optional<User> expected = Optional.of(user);
        underTest.signIn("user","user");
        Optional<UserDto> actual = underTest.describe();

        assertEquals(expected.get().getUsername(),actual.get().username());
        assertEquals(expected.get().getRole(),actual.get().role());

        verify(userRepository).findByUsernameAndPassword("user","user");
    }
}
