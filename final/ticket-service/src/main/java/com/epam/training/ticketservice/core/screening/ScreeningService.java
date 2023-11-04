package com.epam.training.ticketservice.core.screening;

import java.util.List;

public interface ScreeningService {

    public void createScreening(String movieName, String roomName,String date);

    public void deleteScreening();

    public List<Screening> listScreening();
}
