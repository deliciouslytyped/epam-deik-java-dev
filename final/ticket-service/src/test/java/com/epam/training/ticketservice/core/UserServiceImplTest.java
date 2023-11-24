package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.user.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    private final UserService underTest = new UserServiceImpl(userRepository);

    @Test
    void testSignInPrivilegedShouldSetSignedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        User user = new User("user","password", Role.ADMIN);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword("user","pass")).thenReturn(Optional.of(user));

        //When
        Optional<UserDto> actual = underTest.signInPrivileged("user","pass");

        //Then
        assertEquals(expected.get().getUsername(), actual.get().username());
        assertEquals(expected.get().getRole(), actual.get().role());
        verify(userRepository).findByUsernameAndPassword("user","pass");
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
        verify(userRepository).findByUsernameAndPassword("dummy","dummy");
    }

    @Test
    void testSignInShouldSetSignedInUserWhenUsernameAndPasswordAreCorrect() {
        //Given
        User user = new User("username","password", Role.USER);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword("user","pass")).thenReturn(Optional.of(user));

        //When
        Optional<UserDto> actual = underTest.signIn("user","pass");

        //Then
        assertEquals(expected.get().getUsername(), actual.get().username());
        assertEquals(expected.get().getRole(), actual.get().role());
        verify(userRepository).findByUsernameAndPassword("user","pass");
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
        verify(userRepository).findByUsernameAndPassword("dummy","dummy");
    }

    @Test
    void testSignOutShouldReturnThePreviouslySignedInUserWhenThereIsASignedInUser() {
        //Given
        User user = new User("user","password",Role.USER);
        when(userRepository.findByUsernameAndPassword("user","pass")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.signIn("user","password");

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
        User user = new User("user", "password", Role.USER);
        when(userRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.signIn("user", "password");

        // When
        Optional<UserDto> actual = underTest.describe();

        // Then
        assertEquals(expected, actual);
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
        User user = new User("root","root",Role.USER);
        when(userRepository.save(user)).thenReturn(user);

        //When
        underTest.signUp(user.getUsername(), user.getPassword());

        //Then
        verify(userRepository).save(user);
    }
}
