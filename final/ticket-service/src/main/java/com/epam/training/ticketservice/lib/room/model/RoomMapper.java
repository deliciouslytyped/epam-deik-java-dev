package com.epam.training.ticketservice.lib.room.model;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Mapper
public abstract class RoomMapper extends CustomMapper<RoomDto, Room, String> {
    @Autowired // TODO mapstruct doesnt work with constructor injection?
    protected RoomCrudRepository rr;

    @SneakyThrows
    @Override
    public Room entityFromId(@NonNull String s) {
        return rr.findById(s).orElseThrow(() -> new NoSuchRecordException(s));
    }

    @Override
    public RoomDto dtoFromStrings(@NonNull String... strings) {
        return new RoomDto(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
    }

    @Override
    public RoomDto dtoFromJSON(@NonNull JsonNode attrs) {
        return dtoFromStrings(
                attrs.get("name").textValue(),
                attrs.get("rowCount").textValue(),
                attrs.get("colCount").textValue()
        );
    }

    @Override
    public Integer getStringsCount() {
        return 3;
    }
}
