package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.pricing.PricingService;
import com.epam.training.ticketservice.lib.pricing.persistence.model.TicketPriceDto;
import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Arrays;

import static java.util.FormatProcessor.FMT;

@ShellComponent
@RequiredArgsConstructor
public class PriceCommands implements PrivilegedCommand {
    protected final PricingService ps;
    protected final ScreeningCrudService scs;

    @ShellMethod(key = "update base price")
    public String updateBasePrice(String price){
        ps.updateBasePrice(Integer.valueOf(price));
        return null;
    }

    @ShellMethod(key = "create price component")
    public String createPriceComponent(String name, String price) {
        ps.createPriceComponent(name, Integer.valueOf(price));
        return "";
    }

    @ShellMethod(key = "attach price component to room")
    public String attachPriceComponentToRoom(String name, String room) {
        ps.attachPriceComponentToRoom(name, room);
        return "";
    }

    @ShellMethod(key = "attach price component to movie")
    public String attachPriceComponentToMovie(String name, String movie) {
        ps.attachPriceComponentToMovie(name, movie);
        return "";
    }

    @ShellMethod(key = "attach price component to screening")
    public String attachPriceComponentToScreening(String name, String movieName, String roomName, String time) {
        var sdto = new ScreeningDto(null, movieName, roomName, Util.parseTime(time));
        var screening = scs.getByAlternateKey(scs.getMapper().dtoToAlternateKey(sdto)).get();
        ps.attachPriceComponentToScreening(name, screening);
        return "";
    }

    @ShellMethod(key = "show price for")
    public String showPrice(String title, String room, String time, String seats) {
        var count = Math.toIntExact(Arrays.stream(seats.split(" ")).count());
        var sdto = new ScreeningDto(null, title, room, Util.parseTime(time));
        var screening = scs.getByAlternateKey(scs.getMapper().dtoToAlternateKey(sdto)).get();
        var price = ps.getPrice(new TicketPriceDto(screening, count));
        return FMT."The price for this booking would be \{price} HUF\n";
    }


}