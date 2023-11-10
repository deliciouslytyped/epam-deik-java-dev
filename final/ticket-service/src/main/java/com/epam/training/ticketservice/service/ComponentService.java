package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;

public interface ComponentService {

    Result<?, OperationException> createComponent(String name, int amount);

    Result<?, OperationException> attachToMovie(String componentName, String movieTitle);

    Result<?, OperationException> attachToRoom(String componentName, String roomName);

    Result<?, OperationException> attachToScreening(String componentName, String movieTitle, String roomName,
                                                    String startTime);
}
