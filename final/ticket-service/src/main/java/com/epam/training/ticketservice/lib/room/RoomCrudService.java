package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudService;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;


public interface RoomCrudService extends CustomCrudService<RoomDto, String> {
}
