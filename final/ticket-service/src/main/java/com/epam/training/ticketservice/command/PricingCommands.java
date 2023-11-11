package com.epam.training.ticketservice.command;

import com.epam.training.ticketservice.component.BasePriceHolder;
import com.epam.training.ticketservice.service.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;

@ShellComponent
@RequiredArgsConstructor
public class PricingCommands extends PrivilegedCommands {
    private final ComponentService service;
    private final BasePriceHolder basePriceHolder;

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "update base price", value = "Usage: <base price>")
    public String updateBasePrice(int price) {
        basePriceHolder.setBasePrice(price);
        return "Successfully updated base price";
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create price component", value = "Usage: <name> <amount>")
    public String createComponent(String name, int amount) {
        var res = service.createComponent(name, amount);
        return switch (res.state()) {
            case OK -> "Successfully created price component";
            case ERROR -> "Failed to create price component: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "attach price component to room", value = "Usage: <component name> <room name>")
    public String attachToRoom(String name, String room) {
        var res = service.attachToRoom(name, room);
        return switch (res.state()) {
            case OK -> "Successfully attached price component to room";
            case ERROR -> "Failed to attach price component: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "attach price component to movie", value = "Usage: <component name> <movie title>")
    public String attachToMovie(String name, String movie) {
        var res = service.attachToMovie(name, movie);
        return switch (res.state()) {
            case OK -> "Successfully attached price component to movie";
            case ERROR -> "Failed to attach price component: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "attach price component to screening",
            value = "Usage: <component name> <movie title> <room name> <start time>")
    public String attachToScreening(String name, String movieTitle, String roomName, String startTime) {
        var res = service.attachToScreening(name, movieTitle, roomName, startTime);
        return switch (res.state()) {
            case OK -> "Successfully attached price component to screening";
            case ERROR -> "Failed to attach price component: " + res.error().getMessage();
        };
    }
}
