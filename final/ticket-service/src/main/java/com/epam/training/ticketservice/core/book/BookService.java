package com.epam.training.ticketservice.core.book;

import com.epam.training.ticketservice.core.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookService {

    void createBook(String movieName, String roomName, String date, List<String> seats);

    void updateBasePrice(int newBasePrice);

    int getBasePrice();
}
