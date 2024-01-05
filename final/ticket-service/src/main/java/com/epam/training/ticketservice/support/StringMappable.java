package com.epam.training.ticketservice.support;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.NonNull;

// TODO using default methods to prevent mapstruct generation...
public interface StringMappable<DTO> { //TODO https://github.com/mapstruct/mapstruct/discussions/3507
    //TODO I dont like how this is implemented
    default DTO dtoFromStrings(@NonNull String... strings){
        throw new UnsupportedOperationException();
    };
    default DTO dtoFromKeyStrings(@NonNull String... strings){
        throw new UnsupportedOperationException();
    };

    default DTO dtoFromJSON(@NonNull JsonNode attrs){
        throw new UnsupportedOperationException();
    }; //TODO Pulls in JSON dependency, need to move this somewhere else
    default Integer getStringsCount() {
        throw new UnsupportedOperationException();
    };

}
