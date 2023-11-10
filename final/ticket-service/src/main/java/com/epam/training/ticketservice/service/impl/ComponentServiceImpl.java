package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ComponentRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.ComponentService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComponentServiceImpl implements ComponentService {
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;
    private final ComponentRepository componentRepository;

    @Override
    public Result<?, OperationException> createComponent(String name, int amount) {
        if (componentRepository.existsByName(name)) {
            return Result.err(new AlreadyExistsException("Price component"));
        }
        componentRepository.save(new PriceComponent(name, amount));
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> attachToMovie(String componentName, String movieTitle) {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            return Result.err(new NotFoundException("Movie"));
        }
        var comp = componentRepository.findByName(componentName);
        if (comp.isEmpty()) {
            return Result.err(new NotFoundException("Price component"));
        }
        movie.get().setPriceComponent(comp.get());
        movieRepository.save(movie.get());
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> attachToRoom(String componentName, String roomName) {
        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            return Result.err(new NotFoundException("Room"));
        }
        var comp = componentRepository.findByName(componentName);
        if (comp.isEmpty()) {
            return Result.err(new NotFoundException("Price component"));
        }
        room.get().setPriceComponent(comp.get());
        roomRepository.save(room.get());
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> attachToScreening(String componentName, String movieTitle, String roomName,
                                                           String startTime) {
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName,
                LocalDateTime.parse(startTime, Screening.TIME_FORMAT));
        if (screening.isEmpty()) {
            return Result.err(new NotFoundException("Screening"));
        }
        var comp = componentRepository.findByName(componentName);
        if (comp.isEmpty()) {
            return Result.err(new NotFoundException("Price component"));
        }
        screening.get().setPriceComponent(comp.get());
        screeningRepository.save(screening.get());
        return Result.ok(null);
    }
}
