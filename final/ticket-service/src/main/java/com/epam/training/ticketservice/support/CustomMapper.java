package com.epam.training.ticketservice.support;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.NonNull;

import java.util.function.Function;

public abstract class CustomMapper<DTO,ENTITY,ID> implements StringMappable<DTO> {
    public abstract ENTITY dtoToEntity(@NonNull DTO entityDto); // This is the unsafe direction //TODO bijection object //TODO? out of the two operations this is bijectionally unsafe
    public abstract DTO entityToDto(@NonNull ENTITY entity);

    public abstract ENTITY entityFromId(@NonNull ID id);
    public static <T> T orNull(Function<String,T> f, String v) {
        if (v.equals("null")) {
            return null;
        } else {
            return f.apply(v);
        }
    }
}
