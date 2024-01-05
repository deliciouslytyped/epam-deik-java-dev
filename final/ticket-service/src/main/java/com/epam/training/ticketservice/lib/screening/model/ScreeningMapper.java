package com.epam.training.ticketservice.lib.screening.model;

import com.epam.training.ticketservice.lib.movie.MovieCrudService;
import com.epam.training.ticketservice.lib.movie.model.MovieMapper;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.room.RoomCrudService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.room.model.RoomMapper;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

@Mapper(uses = {MovieMapper.class, RoomMapper.class}) //TODO probably needs some fiddling due to the key and alternate key
public abstract class ScreeningMapper extends CustomMapper<ScreeningDto, Screening, Long> {
    @Autowired // TODO mapstruct doesnt work with constructor injection?
    protected ScreeningCrudRepository sr;

    //TODO is this necessary?
    @Mappings({
        @Mapping(source = "movieTitle", target = "movie"),
        @Mapping(source = "roomName", target = "room"),
        @Mapping(source = "time", target = "startTime"),
    })
    public abstract BaseScreening dtoToAlternateKey(ScreeningDto dto);

    //TODO do I really need to override all these?
    //TODO I need to figure out if this is supposed to be retrieving or creating, and if its only one of the two, what do you do for the other case?
    // I guess dtos created from entities would need to have a backreference to their entity (basically caching?) and dtos created out of thin-air need this lookup
    @SneakyThrows
    @Override
    public Screening dtoToEntity(@NonNull ScreeningDto entityDto){
        return sr.getByAlternateKey(Screening.class, dtoToAlternateKey(entityDto)).orElseThrow(() -> new NoSuchRecordException(entityDto.toString()));
    };

    @Override
    @Mappings({
            @Mapping(source = "screening.movie.title", target = "movieTitle"),
            @Mapping(source = "screening.room.name", target = "roomName"),
            @Mapping(source = "screening.startTime", target = "time"),
    })
    public abstract ScreeningDto entityToDto(@NonNull Screening screening);

    @SneakyThrows
    @Override
    public Screening entityFromId(@NonNull Long id) {
        return sr.findById(id).orElseThrow(() -> new NoSuchRecordException(id.toString()));
    }

    @Override
    public ScreeningDto dtoFromStrings(@NonNull String... strings) {
        return new ScreeningDto(strings[0], strings[1], Instant.parse(strings[2]));
    }

    @Override
    public ScreeningDto dtoFromJSON(@NonNull JsonNode attrs) {
        return dtoFromStrings(
                Objects.requireNonNull(attrs.get("movietitle").textValue()),
                Objects.requireNonNull(attrs.get("roomname").textValue()),
                Objects.requireNonNull(attrs.get("time").textValue())
        );
    }

    @Override
    public Integer getStringsCount() {
        return 3;
    }
}
