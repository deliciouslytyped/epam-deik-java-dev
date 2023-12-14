package com.epam.training.ticketservice.lib.db.constraints;

import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class ConstraintFilters {
    public List<ConstraintViolationHandler.ConstraintType> enums;
    public List<String> cnames;

    public static ConstraintFilters of(List<ConstraintViolationHandler.ConstraintType> ct, List<String> cnames){
        return new ConstraintFilters(ct, cnames);
    }
}

