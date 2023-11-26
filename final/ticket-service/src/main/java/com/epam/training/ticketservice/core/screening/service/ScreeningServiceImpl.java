package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

    @Override
    public String createScreening(String movieName, String roomName, String date) {
        Screening createScreening = new Screening(movieName,roomName,date);

        List<Screening> overlaps = new ArrayList<>();
        List<Screening> breaks = new ArrayList<>();
        if (movieRepository.findByTitle(movieName).isPresent()) {
            if (roomRepository.findByName(roomName).isPresent()) {
                listScreening() //TODO ha üres az kérdéses hogy lefut-e
                        .forEach(screening -> {

                            if (screening.getRoomName().equals(roomName)) {
                                if (isOverlap(screening, createScreening)) {
                                    System.out.println("isOverlap true");
                                    overlaps.add(screening);
                                }
                                if (isOnBreak(screening, createScreening)) {
                                    System.out.println("isOnbreak true");
                                    breaks.add(screening);
                                }
                            }
                        });
            } else {
                return "Room not exists";
            }
        } else {
            return "Movie not exists";
        }

        if (!overlaps.isEmpty()) {
                return "There is an overlapping screening";
            }
            if (!breaks.isEmpty()) {
                return "This would start on the break period after another screening in this room";
            }
            screeningRepository.save(createScreening);
            return movieName + " " + roomName + " " + date + " screening has been created!";
        }


    public LocalDateTime convertEndOfScreening(Screening screening){
        return screening.getDate()
                .plusMinutes(movieRepository
                        .findByTitle(screening
                        .getMovieName())
                        .map(Movie::getLength)
                        .orElse(0));
    }

    public boolean isOverlap(Screening screening, Screening createScreening) {

        LocalDateTime screeningStart = screening.getDate();
        LocalDateTime screeningEnd = convertEndOfScreening(screening);

        LocalDateTime createScreeningStart = createScreening.getDate();
        LocalDateTime createScreeningEnd = convertEndOfScreening(createScreening);

        return  !((createScreeningStart.isAfter(screeningStart)
                && (createScreeningStart.isAfter(screeningEnd)))
                || ((createScreeningEnd.isBefore(screeningStart))
                && createScreeningEnd.isBefore(screeningEnd)));

    } //TODO MINDENT KITÖRÖL HA VAN HA NINCS

    public boolean isOnBreak(Screening screening,Screening createScreening) {
        LocalDateTime screeningStart = screening.getDate();
        LocalDateTime screeningEnd = convertEndOfScreening(screening).plusMinutes(10);

        LocalDateTime createScreeningStart = createScreening.getDate();
        LocalDateTime createScreeningEnd = convertEndOfScreening(createScreening).plusMinutes(10);
         return !((createScreeningStart.isAfter(screeningEnd)
                || createScreeningStart.isEqual(screeningEnd))
                || ((createScreeningEnd.isBefore(screeningStart)
                || createScreeningEnd.isEqual(screeningStart))));
    }


    @Override
    @Transactional
    public void deleteScreening(String movieName, String roomName,String date) {
        Screening screening = new Screening(movieName,roomName,date);
        screeningRepository.deleteByMovieNameAndRoomNameAndDate(movieName,roomName,screening.getDate());
    }

    @Override
    public Optional<Screening> findScreening(String movieName, String roomName, String date) {
        return screeningRepository.findByMovieNameAndRoomNameAndDate(movieName, roomName, date);
    }

    @Override
    public List<Screening> listScreening() {
        return screeningRepository.findAll();
    }
}
