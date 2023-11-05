package com.epam.training.ticketservice.core.book;

import java.util.Date;
import java.util.List;

public interface BookService {

    public String book(String title, String roomName, Date date, List<String> seats);
}
