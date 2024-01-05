package com.epam.training.ticketservice.lib.ticket.model;

import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.lib.ticket.persistence.Ticket;
import com.epam.training.ticketservice.lib.ticket.persistence.TicketCrudRepository;
import com.epam.training.ticketservice.support.CustomMapper;
import com.epam.training.ticketservice.support.exceptions.NoSuchRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Mapper(uses=ScreeningMapper.class)  //TODO I need to do this for nested dto objects? this could pull the full hierarchy in?
public abstract class TicketMapper extends CustomMapper<TicketDto, Ticket, Long> {
    @Autowired
    protected TicketCrudRepository cr;

    @Autowired
    protected ScreeningMapper sm;

    @SneakyThrows
    @Override
    public Ticket entityFromId(@NonNull Long id){
        return cr.findById(id).orElseThrow(() -> new NoSuchRecordException(id.toString()));
    }

    @Override
    //So so far we have like...3 different modalities of dtos?
    // ones (like here) where we have data along with the key and  the key needs to come from the database so nullable, to be filled in / from the entity?
    // ones where we have data along with the key but the key carries semantic info, like movie or room with title and name
    // ones where the entire dto is the key, but there are also alternate keys, like screening
    // ...?
    // or is this more a matter of whether its in the dto layer?
    public TicketDto dtoFromStrings(@NonNull String... strings) {
        var screeningDto = sm.dtoFromStrings(strings[2], strings[3], strings[4]);
        return new TicketDto(orNull(Long::parseLong, strings[0]), Integer.parseInt(strings[1]), screeningDto);
    }

    @Override
    public TicketDto dtoFromJSON(@NonNull JsonNode attrs) {
        var screeningJson = attrs.get("screening");
        var screening = sm.dtoFromJSON(screeningJson);
        return dtoFromStrings(
                attrs.get("ticketid").textValue(), //or null?
                attrs.get("paid").textValue(),
                screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getTime().toString()
        );
    }

    @Override
    public Integer getStringsCount() {
        return 5;
    }
}