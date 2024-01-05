package com.epam.training.ticketservice.lib.reservation.model;

import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationCrudRepository;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.lib.ticket.model.TicketMapper;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper(uses = {SeatMapper.class, ScreeningMapper.class, TicketMapper.class})
public abstract class ReservationMapper extends CustomMapper<ReservationDto, Reservation, ReservationKey> {
    @Autowired
    protected ReservationCrudRepository rr;

    @Autowired
    protected SeatMapper sm;

    @Autowired
    protected ScreeningMapper scm;

    @Mappings({
            @Mapping(target = "reservationFor.booking.ticketId", source = "ticketId"),
            @Mapping(target = "reservationFor.seat", source = "seat"),
            @Mapping(target = "reservationFor.screening", source = "screening")
    })
    @Override
    public abstract Reservation dtoToEntity(@NonNull ReservationDto entityDto);

    @Mappings({ //TODO check if theres a way to spec reservationfor.*
            @Mapping(source = "reservationFor.booking.ticketId", target = "ticketId"),
            @Mapping(source = "reservationFor.seat", target = "seat"),
            @Mapping(source = "reservationFor.screening", target = "screening")
    })
    @Override
    public abstract ReservationDto entityToDto(@NonNull Reservation reservation);

    @SneakyThrows
    @Override
    public Reservation entityFromId(@NonNull ReservationKey id) {
        return rr.findById(id).orElseThrow(() -> new NoSuchRecordException(id.toString()));
    }

    @Override
    public ReservationDto dtoFromStrings(@NonNull String... s) {
        return new ReservationDto(
                Long.parseLong(s[0]),
                scm.dtoFromStrings(s[1], s[2], s[3]), //TODO fix after fixing schema
                sm.dtoFromStrings(s[4], s[5])
        );
    }

    @Override
    public ReservationDto dtoFromJSON(@NonNull JsonNode attrs) {
        var screeningJson = attrs.get("screening");
        var screening = scm.dtoFromJSON(screeningJson);
        var seatJson = attrs.get("seat");
        var seat = sm.dtoFromJSON(seatJson);
        return dtoFromStrings(
                Objects.requireNonNull(attrs.get("ticketid").textValue()),
                screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getTime().toString(),
                seat.getRowIdx().toString(),
                seat.getColIdx().toString()
        );
    }

    /*@Override
    public ReservationDto dtoFromStringMap(@NonNull Map<String, Object> attrs) { //TODO varargs seats
        var scr = ()(attrs.get("screening"));
        var seats = (String[])(attrs.get("seats"));
        var args = List.of(attrs.get("ticketid"), movietitle, roomname, time);
        args.addAll(seats.)
        return dtoFromStrings(args.toArray(new String[0]));
    }*/

    @Override
    public Integer getStringsCount() {
        return 6;
    }

}
