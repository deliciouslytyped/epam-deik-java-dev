package com.epam.training.ticketservice.core.screening;

import java.util.Date;
import java.util.List;

public interface ScreeningService {

    public void createScreening(String movieName, String roomName, Date date);

    public void deleteScreening(String movieName, String roomName,Date date);

    public List<Screening> listScreening();
}
