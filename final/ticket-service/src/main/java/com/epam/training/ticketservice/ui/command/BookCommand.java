package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.book.model.BookingDto;
import com.epam.training.ticketservice.core.book.model.SeatDto;
import com.epam.training.ticketservice.core.book.service.BookingService;

import com.epam.training.ticketservice.core.user.persistence.Role;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@ShellComponent
@AllArgsConstructor
public class BookCommand {

    private final BookingService bookingService;
    private final UserService userService;


    @ShellMethodAvailability("isUser")
    @ShellMethod(key = "book", value = "Book a screening.")
    public String book(String movieName, String roomName, LocalDateTime date, String seatsInput) {

        List<SeatDto> seats = Arrays.stream(seatsInput.split(" "))
                .map(seatString -> seatString.split(","))
                .map(seatString -> new SeatDto(parseInt(seatString[0]), parseInt(seatString[1])))
                .toList();
        System.out.println("createbooking eljutunk");
        bookingService.createBook(userService.describe().get().username(),
                movieName,roomName,date.toString(),seats);//TODO
        System.out.println("idáig nem");
        String result = seats.stream().map(seat ->
                "(" + seat.row() + "," + seat.col() + ")"
        ).collect(Collectors.joining(", "));

        return "Seats booked: " + result + "; the price for this booking is "
                + seats.size() * bookingService.getBasePrice() + " HUF";
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "update base price",value = "Updates the base price")
    public String updateBasePrice(int price) {
        bookingService.updateBasePrice(price);
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
