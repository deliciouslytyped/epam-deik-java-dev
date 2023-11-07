package com.epam.training.ticketservice.core.screening;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;

    @Override
    public void createScreening(String movieName, String roomName, String date) {
        screeningRepository.save(new Screening(movieName,roomName,date));
    }

    @Override
    @Transactional
    public void deleteScreening(String movieName, String roomName,String date) {
        Screening screening = new Screening(movieName,roomName,date);
        screeningRepository.deleteByMovieNameAndRoomNameAndDate(movieName,roomName,screening.getDate());
    }

    @Override
    public List<Screening> listScreening() {
        return screeningRepository.findAll();
    }
}
