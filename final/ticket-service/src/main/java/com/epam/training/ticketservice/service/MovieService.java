package com.epam.training.ticketservice.service;

public interface MovieService {

    void createMovie(String title, String category, int length);

    void updateMovie(String title, String category, int length);

    void deleteMovie(String title);
}
