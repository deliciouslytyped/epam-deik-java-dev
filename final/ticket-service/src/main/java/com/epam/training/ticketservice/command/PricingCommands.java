package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;

@ShellComponent
public class PricingCommands extends PrivilegedCommands {

    @ShellMethod(key = "update base price", value = "Usage: <base price>")
    public String updateBasePrice(int price) {
        return "";
    }

    @ShellMethod(key = "create price component", value = "Usage: <name> <amount>")
    public String createComponent(String name, int amount) {
        return "";
    }

    @ShellMethod(key = "attach price component to room", value = "Usage: <component name> <room name>")
    public String attachToRoom(String name, String room) {
        return "";
    }

    @ShellMethod(key = "attach price component to movie", value = "Usage: <component name> <movie title>")
    public String attachToMovie(String name, String movie) {
        return "";
    }

    @ShellMethod(key = "attach price component to screening",
            value = "Usage: <component name> <movie title> <room name> <start time>")
    public String attachToScreening(String name, String screening) {
        return "";
    }

    @ShellMethod(key = "show price for", value = "Usage: <movie title> <room name> <start time> <seats>")
    public String showPricing(String movie, String room, LocalDateTime start, String seats) {
        return "";
    }
}
