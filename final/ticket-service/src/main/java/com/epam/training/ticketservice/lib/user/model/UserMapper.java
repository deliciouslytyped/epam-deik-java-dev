package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.reservation.model.ReservationDto;
import com.epam.training.ticketservice.lib.reservation.model.ReservationMapper;
import com.epam.training.ticketservice.lib.reservation.model.SeatMapper;
import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationCrudRepository;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.lib.ticket.model.TicketMapper;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Mapper(uses = {TicketMapper.class})
public abstract class UserMapper extends CustomMapper<UserDto, ApplicationUser, Long> {
    @Autowired
    protected ApplicationUserCrudRepository ur;

    @SneakyThrows
    @Override
    public ApplicationUser entityFromId(@NonNull Long id) {
        return ur.findByUidWithBookings(id).orElseThrow(() -> new NoSuchRecordException(id.toString()));
    }

    //TODO cant really deal with the varargs tickets portion
    @Override
    public UserDto dtoFromStrings(@NonNull String... s) {
        return new UserDto(Long.parseLong(s[0]), s[1]); //TODO username as alternate key of uid?
    }

    @Override
    public Integer getStringsCount() {
        return 3;
    }

    @Override
    public UserDto dtoFromJSON(@NonNull JsonNode attrs) {
        return dtoFromStrings(
                attrs.get("uid").textValue(), //or null?
                attrs.get("username").textValue()
        );
    }


    @Mapping(target = "uname", source = "username")
    @Override
    public abstract UserDto entityToDto(@NonNull ApplicationUser applicationUser);
}
