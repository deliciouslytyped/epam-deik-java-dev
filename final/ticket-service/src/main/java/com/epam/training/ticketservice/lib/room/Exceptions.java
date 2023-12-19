package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;

    public class Exceptions {
        public static void throwPositiveRow(RoomDto r){
            throw new ApplicationDomainException("The number of rows in a room must be a positive number.");
        }

        public static void throwPositiveCol(RoomDto r){
            throw new ApplicationDomainException("The number of columns in a room must be a positive number.");
        }

        public static void throwAlreadyExists(RoomDto arg){
            throw new AlreadyExistsException("The room " + arg.getName());
        }

        public static void throwNotExist(RoomDto arg){
            throw new ApplicationDomainException("The room " + arg.getName() + " does not exist.");
        }

        public static void throwNotExistWithContext(String s, Exception e) {
            throw new ApplicationDomainException("The room " + s + " does not exist.", e);
        }
    }
