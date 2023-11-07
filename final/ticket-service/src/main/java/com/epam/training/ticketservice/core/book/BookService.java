package com.epam.training.ticketservice.core.book;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookService {

    void createBook(String movieName, String roomName, String date, List<Integer> seats);

    void updateBasePrice(int newBasePrice);
    int getBasePrice();
}
