package com.epam.training.ticketservice.model;

import java.util.ArrayList;
import java.util.List;

public record Seat(int row, int column) {

    public static List<Seat> fromString(String source) {
        var result = new ArrayList<Seat>();
        var seats = source.split("\\s+");
        for (var seat : seats) {
            if (seat.isBlank()) continue;
            var data = seat.split(":");
            result.add(new Seat(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
        }
        return result;
    }
}
