package com.epam.training.ticketservice.test.service;

import com.epam.training.ticketservice.dto.UserDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.model.UserRole;
import com.epam.training.ticketservice.repository.UserRepository;
import com.epam.training.ticketservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication auth;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("admin", "admin", UserRole.ADMIN);
        user.setBookings(List.of());
    }

    @Test
    void testUserCreation() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        var result = userService.createUser(user.getUsername(), user.getPassword());
        assertThat(result.isOk()).isTrue();
        verify(userRepository).save(any());
    }

    @Test
    void testUserCreationFailsWhenUserAlreadyExists() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        var result = userService.createUser(user.getUsername(), user.getPassword());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(AlreadyExistsException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(auth.getName()).thenReturn(user.getUsername());

        var result = userService.getUser(auth);
        assertThat(result.isOk()).isTrue();
        assertThat(result.result()).isEqualTo(new UserDto(user));
    }

    @Test
    void testGetUserFailsWhenUserDoesNotExist() {
        var result = userService.getUser(null);
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
    }
}
