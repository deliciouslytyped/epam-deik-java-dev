package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.book.Book;
import com.epam.training.ticketservice.core.book.BookService;

import com.epam.training.ticketservice.core.user.Role;
import com.epam.training.ticketservice.core.user.UserDto;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class BookCommand {

    private final BookService bookService;
    private final UserService userService;


    @ShellMethodAvailability("isUser")
    @ShellMethod(key = "book", value = "Book a screening.")
    public String book(String movieName, String roomName, String date, List<Integer> seats){
        bookService.createBook(movieName,roomName,date,seats);
        return "Seats booked: " + seats + "; the price for this booking is " + seats.size() * bookService.getBasePrice() + " HUF";
    }

    @ShellMethod(key = "update base price",value = "Updates the base price")
    public String updateBasePrice(int price){
        bookService.updateBasePrice(price);
        return "Base price updated to " + price;
    }

    private Availability isAdmin() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }

    private Availability isUser() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.USER
                ? Availability.available()
                : Availability.unavailable("You are not a user!");
    }
}
