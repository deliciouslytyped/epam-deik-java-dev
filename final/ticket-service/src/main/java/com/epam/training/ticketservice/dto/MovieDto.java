package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Movie;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MovieDto {
    private final String title;
    private final String category;
    private final int length;

    public MovieDto(Movie dao) {
        this.title = dao.getTitle();
        this.category = dao.getCategory();
        this.length = dao.getLength();
    }

    @Override
    public String toString() {
        return title + " (" + category + ", " + length + " minutes)";
    }
}
