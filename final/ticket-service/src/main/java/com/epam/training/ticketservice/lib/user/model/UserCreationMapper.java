package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.ticket.model.TicketMapper;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.StringMappable;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Mapper
public abstract class UserCreationMapper implements StringMappable<UserCreationDto> {
    @Override
    public UserCreationDto dtoFromStrings(@NonNull String... s) {
        return new UserCreationDto(s[0], s[1]);
    }

    @Override
    public Integer getStringsCount() {
        return 3;
    }

    @Override
    public UserCreationDto dtoFromJSON(@NonNull JsonNode attrs) {
        return dtoFromStrings(
                attrs.get("username").textValue(),
                attrs.get("password").textValue()
        );
    }
}
