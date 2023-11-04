package com.epam.training.ticketservice.core.screening;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService{

    private final ScreeningRepository screeningRepository;

    @Override
    public void createScreening(String movieName, String roomName, String date) {
        screeningRepository.save(new Screening(movieName,roomName,date));
    }

    @Override
    public void deleteScreening(String movieName, String roomName,String date) {
        screeningRepository.deleteByMovieNameAndRoomNameAndDate(movieName,roomName,date);
    }

    @Override
    public List<Screening> listScreening() {
        return screeningRepository.findAll();
    }
}
