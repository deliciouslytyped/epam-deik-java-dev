package com.epam.training.ticketservice.lib.reservation.model;

import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationCrudRepository;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.reservation.persistence.Seat;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Mapper
public abstract class SeatMapper {
    public abstract SeatDto weakEntityToDto(Seat s);
    public abstract Seat dtoToWeakEntity(SeatDto s);

    @Mapping(target = "rowIdx", source = "s1")
    @Mapping(target = "colIdx", source = "s2")
    public abstract SeatDto dtoFromStrings(String s1, String s2); //TODO with array?

    public SeatDto dtoFromJSON(JsonNode n){
        return dtoFromStrings(
            Objects.requireNonNull(n.get("row").textValue()),
            Objects.requireNonNull(n.get("col").textValue()));
    }
}
