package com.epam.training.ticketservice.lib.db.constraints;

public class ConstraintHandlerHolder<T> {
    public ConstraintFilters getConstraintFilters(){
        throw new UnsupportedOperationException("Constraint handling not configured in this holder. Class must be overridden.");
    };
    public void getConstraintTypeHandler(ConstraintViolationHandler.ConstraintType t, T arg) {
        throw new UnsupportedOperationException("Constraint handling not configured in this holder. Class must be overridden.");
    };
    public void getNamedConstraintHandler(String cname, T arg) {
        throw new UnsupportedOperationException("Constraint handling not configured in this holder. Class must be overridden.");
    };
}
