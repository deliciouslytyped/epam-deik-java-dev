package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.book.BookService;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.room.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Date;
import java.util.List;

@ShellComponent
@AllArgsConstructor
public class BookCommand {



    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "book", value = "Book a screening.")
    public String book(String title, String roomName, Date date, List<String> seats){
        return null;
    }
}
