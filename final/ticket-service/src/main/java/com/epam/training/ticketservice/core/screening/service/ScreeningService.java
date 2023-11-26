package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    public String createScreening(String movieName, String roomName, String date);

    public void deleteScreening(String movieName, String roomName,String date);

    public Optional<ScreeningDto> findScreening(String movieName, String roomName, String date);

    public List<ScreeningDto> listScreening();
}
