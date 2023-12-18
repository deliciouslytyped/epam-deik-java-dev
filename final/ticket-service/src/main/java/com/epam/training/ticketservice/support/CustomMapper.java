package com.epam.training.ticketservice.support;

import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;

public interface CustomMapper<DTO,ENTITY> {
    ENTITY dtoToEntity(@NonNull DTO entityDto); // This is the unsafe direction //TODO bijection object //TODO? out of the two operations this is bijectionally unsafe
    DTO entityToDto(@NonNull ENTITY entity);
}
