package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.Movie;
import com.epam.training.ticketservice.core.movie.MovieRepository;
import com.epam.training.ticketservice.core.room.Room;
import com.epam.training.ticketservice.core.room.RoomRepository;
import com.epam.training.ticketservice.core.screening.Screening;
import com.epam.training.ticketservice.core.screening.ScreeningRepository;
import com.epam.training.ticketservice.core.user.Role;
import com.epam.training.ticketservice.core.user.User;
import com.epam.training.ticketservice.core.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InMemoryDatabaseInitializer {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;


    @PostConstruct
    public void init() {
        User admin = new User("admin","admin", Role.ADMIN);
        userRepository.save(admin);
        Movie cars = new Movie("Cars","drama",30);
        movieRepository.save(cars);
        Room szoba = new Room("szoba",10,10);
        roomRepository.save(szoba);
        Screening screening = new Screening("Cars","szoba","2003-01-29 16:00");
        screeningRepository.save(screening);
    }
}