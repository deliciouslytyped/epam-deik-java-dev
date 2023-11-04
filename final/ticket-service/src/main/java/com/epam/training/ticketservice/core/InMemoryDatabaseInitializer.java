package com.epam.training.ticketservice.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InMemoryDatabaseInitializer {
    private final UserRepository userRepository;

    @PostConstruct
    public void init(){
        User admin = new User("admin","admin");
        userRepository.save(admin);
    }
}
