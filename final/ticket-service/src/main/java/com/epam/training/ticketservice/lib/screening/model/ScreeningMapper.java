package com.epam.training.ticketservice.lib.screening.model;

import com.epam.training.ticketservice.lib.movie.MovieCrudService;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.room.RoomCrudService;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.support.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper //TODO probably needs some fiddling due to the key and alternate key
public abstract class ScreeningMapper implements CustomMapper<ScreeningDto, Screening> {
    @Autowired //TODO
    protected MovieCrudRepository mr;

    @Autowired //TODO
    protected RoomCrudRepository rr;

    public BaseScreening dtoToAlternateKey(ScreeningDto dto) {
        var room = rr.findById(dto.getMovieTitle()).get();
        var movie = mr.findById(dto.getRoomName()).get();
        return new BaseScreening(room, movie, dto.getTime());
    }
}
