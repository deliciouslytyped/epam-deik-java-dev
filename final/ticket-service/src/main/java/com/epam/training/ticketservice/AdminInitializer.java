package com.epam.training.ticketservice;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.model.UserRole;
import com.epam.training.ticketservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final UserRepository repository;

    @PostConstruct
    private void createAdmin() {
        repository.save(new User("admin", "admin", UserRole.ADMIN));
    }
}
