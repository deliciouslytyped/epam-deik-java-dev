package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;

@ShellComponent
public class BookingCommands {

    @ShellMethod(key = "book", value = "Usage: <movie title> <room name> <start date> <seats>")
    public String createBooking(String movie, String room, LocalDateTime start, String seats) {
        return "";
    }
}
