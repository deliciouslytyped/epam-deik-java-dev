package com.epam.training.ticketservice.lib.movie;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;

public class Exceptions {
    public static void throwPositiveRuntime(MovieDto arg){
        throw new ApplicationDomainException("The run time of the movie needs to be a positive number.");
    }

    public static void throwNotExist(MovieDto arg){
        throw new ApplicationDomainException("The movie " + arg.getTitle() + " does not exist.");
    }

    public static void throwExists(MovieDto arg){
        throw new AlreadyExistsException("The movie " + arg.getTitle());
    }

    //TODO bit of a hack
    public static void throwNotExistWithContext(String s, Exception e) {
        throw new ApplicationDomainException("The movie " + s + " does not exist.", e);
    }
}
