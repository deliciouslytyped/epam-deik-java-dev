package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Movie;
import lombok.Getter;

@Getter
public class MovieDTO {
    private final String title;
    private final String category;
    private final int length;

    public MovieDTO(Movie dao) {
        this.title = dao.getTitle();
        this.category = dao.getCategory();
        this.length = dao.getLength();
    }

    @Override
    public String toString() {
        return title + " (" + category + ", " + length + " minutes)";
    }
}
