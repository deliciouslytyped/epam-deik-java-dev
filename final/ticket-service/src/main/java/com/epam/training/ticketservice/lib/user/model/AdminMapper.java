package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.reservation.model.ReservationMapper;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationAdmin;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationAdminCrudRepository;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Mapper
public abstract class AdminMapper extends CustomMapper<AdminDto, ApplicationAdmin, Long> {
    @Autowired
    protected ApplicationAdminCrudRepository ar;

    @Mapping(target = "uname", source = "username")
    @Override
    public abstract AdminDto entityToDto(@NonNull ApplicationAdmin applicationUser);

    @SneakyThrows
    @Override
    public ApplicationAdmin entityFromId(@NonNull Long id) {
        return ar.findById(id).orElseThrow(() -> new NoSuchRecordException(id.toString()));
    }

    @Override
    public AdminDto dtoFromStrings(@NonNull String... s) {
        return new AdminDto(Long.parseLong(s[0]), s[1]); //TODO username as alternate key of uid?
    }

    @Override
    public AdminDto dtoFromJSON(@NonNull JsonNode attrs) {
        return dtoFromStrings(
                attrs.get("uid").textValue(), //or null?
                attrs.get("username").textValue()
        );
    }

    @Override
    public Integer getStringsCount() {
        return 3;
    }

}
