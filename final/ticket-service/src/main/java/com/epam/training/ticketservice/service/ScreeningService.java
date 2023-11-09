package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.ScreeningDTO;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    Result<?, OperationException> createScreening(String movie, String room, LocalDateTime start);

    Result<?, OperationException> deleteScreening(String movie, String room, LocalDateTime start);

    Result<List<ScreeningDTO>, OperationException> listScreenings();
}
