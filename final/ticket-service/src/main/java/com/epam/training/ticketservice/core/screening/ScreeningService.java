package com.epam.training.ticketservice.core.screening;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ScreeningService {

    public void createScreening(String movieName, String roomName, String date);

    public void deleteScreening(String movieName, String roomName,String date);

    public List<Screening> listScreening();
}
