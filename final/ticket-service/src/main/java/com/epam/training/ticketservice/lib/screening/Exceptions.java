package com.epam.training.ticketservice.lib.screening;

import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;

public class Exceptions {

    public static void throwNotExist(ScreeningDto arg){
        throw new ApplicationDomainException("The screening for " + arg.getMovieTitle() + " in " + arg.getRoomName() + " at " + arg.getTime() + " does not exist.");
    }

    public static void throwOverlap(ScreeningDto arg){
        throw new ApplicationDomainException("You can't create a screening that overaps with another screening in the same room.");
    }

    public static void throwOverlapBreak(ScreeningDto arg){
        throw new ApplicationDomainException("You can't create a screening that overlaps with the 10 minute break after another screening.");
    }

    public static void throwExists(ScreeningDto arg){
        throw new AlreadyExistsException("The screening for " + arg.getMovieTitle() + " in " + arg.getRoomName() + " at " + arg.getTime() + " already exists.");
    }

    //TODO bit of a hack
    public static void throwNotExistWithContext(String s, Exception e) {
        throw new ApplicationDomainException("The screening " + s + " does not exist.", e); //TODÍO
    }

    public static void throwPast(ScreeningDto arg){
        throw new ApplicationDomainException("You can't create a screening in the past.");
    }
}
