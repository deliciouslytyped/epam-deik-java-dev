package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository repository;
    private final MovieService movieService;
    private final RoomService roomService;

    @Override
    public Result<?, OperationException> createScreening(String movie, String room, LocalDateTime start) {
        if (repository.existsByMovieTitleAndRoomNameAndStartTime(movie, room, start)) {
            return Result.err(new AlreadyExistsException("Screening"));
        }
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> deleteScreening(String movie, String room, LocalDateTime start) {
        if (!repository.existsByMovieTitleAndRoomNameAndStartTime(movie, room, start)) {
            return Result.err(new NotFoundException("Screening"));
        }
        return Result.ok(null);
    }

    @Override
    public Result<List<ScreeningDto>, OperationException> listScreenings() {
        return Result.ok(repository.findAll().stream().map(ScreeningDto::new).toList());
    }
}
