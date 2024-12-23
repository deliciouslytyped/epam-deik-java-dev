package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.pricing.PricingService;
import com.epam.training.ticketservice.lib.pricing.persistence.model.TicketPriceDto;
import com.epam.training.ticketservice.lib.reservation.ReservationCrudServiceImpl;
import com.epam.training.ticketservice.lib.reservation.SeatAlreadyReserved;
import com.epam.training.ticketservice.lib.reservation.model.SeatDto;
import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.ticket.TicketCrudServiceImpl;
import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.lib.user.UserAccountCrudService;
import com.epam.training.ticketservice.lib.user.UserAccountCrudServiceImpl;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.epam.training.ticketservice.presentation.cli.Util.parseTime;
import static java.util.FormatProcessor.FMT;

@ShellComponent
@RequiredArgsConstructor
public class BookingCommands implements PrivilegedCommand {
    private final TicketCrudServiceImpl ts;
    private final ReservationCrudServiceImpl rs;
    private final ScreeningCrudService ss;
    private final PricingService ps;
    private final UserAccountCrudService us;

    @ShellMethod(key = "book")
    public String book(String movie, String room, String date, String seats) { //TODO this should be split into the parsing, and passing args to the service that actually deals with db ops
        var auth = PrivilegedCommand.getAuth(); // Should be nonnull because this command already requires being authed
        var uname = PrivilegedCommand.getUsername(auth);

        try {
            return doBooking(uname, movie, room, date, seats);
        } catch (ApplicationDomainException e) {
            return e.getMessage();
        }
    }

    //TODO this whole thing is a mess, it would probably help to have it in the service layer instead of passing dtos around?
    @Transactional(transactionManager = "JpaTransactionManager") //TODO not sure if still important/makes sense
    //@Transactional
    //Needs to be public for @Transactional per intellij
    public String doBooking(String uname, String movie, String room, String date, String seats) {

        var screeningDto = ss.getByAlternateKey(
                ss.getMapper().dtoToAlternateKey(
                        new ScreeningDto(null, movie, room, parseTime(date)) //TODO we need to deal with all objects requireing an id field anyway...due to neeeding to be able to connect /construct already existing dtos to crap in the hibernate cache? basically, the data model is currently messed up and needs to be figured out...
                )).get();//TODO wrong format?

        var seats_ = Arrays.stream(seats.split(" ")).toList();
        var price = ps.getPrice(new TicketPriceDto(screeningDto, seats_.size()));

        AtomicReference<TicketDto> tdto_ = new AtomicReference<>(new TicketDto(null, price, screeningDto)); //TODO HACK this is a messed up way to do this. We update the price after we create the tdto object, which we use to calculate the price.
        tdto_.set(ts.create(tdto_.get())); //TODO does this fill in the id?
        seats_.forEach(s -> {
            var subs = s.split(",");
            var row = Integer.valueOf(subs[0]);
            var col = Integer.valueOf(subs[1]);
            try {
                rs.addSeat(tdto_.get(), screeningDto, row, col);
            } catch (SeatAlreadyReserved e) {
                throw new ApplicationDomainException(FMT."Seat \{new SeatDto(row, col).toPairString()} is already taken");
            }
        });
        var tdto = tdto_.get();

        //tdto = ts.get(tdto.getTicketId()).get(); // now we should have the reservations field set on the dto..
        //TODO well this doesnt work for some reason
        //ts.update(new TicketDto(tdto.getTicketId(), price, tdto.getScreening()));
        ((UserAccountCrudServiceImpl)us).addTicket(uname, tdto);
        //ts.setPrice(tdto, price);

        //TODO misleading
        var successfulReservations = ts.get(tdto.getTicketId()).orElseThrow(() -> new RuntimeException("wtf2")).getReservations();
        String seatsRepr = successfulReservations.stream()
                .map(r -> r.getSeat().toPairString())
                .collect(Collectors.joining(", "));
        return FMT."Seats booked: \{seatsRepr}; the price for this booking is \{price} HUF";
    }
}