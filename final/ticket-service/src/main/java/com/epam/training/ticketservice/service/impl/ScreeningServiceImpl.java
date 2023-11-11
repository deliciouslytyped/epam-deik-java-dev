package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    @Override
    public Result<?, OperationException> createScreening(String movieTitle, String roomName, String start) {
        var startDate = LocalDateTime.parse(start, Screening.TIME_FORMAT);
        if (screeningRepository.existsByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName, startDate)) {
            return Result.err(new AlreadyExistsException("Screening"));
        }
        var movieOpt = movieRepository.findByTitle(movieTitle);
        if (movieOpt.isEmpty()) {
            return Result.err(new NotFoundException("Movie"));
        }
        var roomOpt = roomRepository.findByName(roomName);
        if (roomOpt.isEmpty()) {
            return Result.err(new NotFoundException("Room"));
        }
        var screening = new Screening(movieOpt.get(), roomOpt.get(), startDate);

        var inRoom = screeningRepository.findAllByRoomName(roomName);
        if (!inRoom.isEmpty()) {
            for (var other : inRoom) {
                if (screening.isOverlapping(other, 10)) {
                    if (screening.isOverlapping(other, 0)) {
                        return Result.err(new OperationException("There is an overlapping screening"));
                    } else {
                        return Result.err(new OperationException(
                                "This would start in the break period after another screening in this room"));
                    }
                }
            }
        }

        screeningRepository.save(screening);
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> deleteScreening(String movie, String room, String start) {
        var startDate = LocalDateTime.parse(start, Screening.TIME_FORMAT);
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie, room, startDate);
        if (screening.isEmpty()) {
            return Result.err(new NotFoundException("Screening"));
        }
        screeningRepository.delete(screening.get());
        return Result.ok(null);
    }

    @Override
    public Result<List<ScreeningDto>, OperationException> listScreenings() {
        return Result.ok(screeningRepository.findAll().stream().map(ScreeningDto::new).toList());
    }
}
