package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.room.model.RoomDto;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    public void create(@NonNull String name, @NonNull Integer rowCount, @NonNull Integer colCount);
    public void update(@NonNull String name, @NonNull Integer rowCount, @NonNull Integer colCount);
    public void delete(@NonNull String name);
    public List<RoomDto> list();

    public Optional<RoomDto> get(@NonNull String name);
}
