package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    public void create(@NonNull String title, @NonNull String genre, @NonNull int runtime);
    public void update(@NonNull String title, @NonNull String genre, @NonNull int runtime);
    public void delete(@NonNull String title);
    public List<MovieDto> list();
    public Optional<MovieDto> get(@NonNull String title);
}
