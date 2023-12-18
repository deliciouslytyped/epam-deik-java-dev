package com.epam.training.ticketservice.support.db.constraints;

import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class ConstraintHandlerHolder<T> {
    public static <Z> ConstraintHandlerHolder<Z> createConstraintHandler(@Nullable Map<ConstraintViolationHandler.ConstraintType, Consumer<Z>> enumhandlers,
                                                                         @Nullable Map<String, Consumer<Z>> stringhandlers){
        return new ConstraintHandlerHolder<Z>() {
            @Override
            public ConstraintFilters getConstraintFilters() {
                return ConstraintFilters.of(enumhandlers != null ? enumhandlers.keySet().stream().toList() : null,
                        stringhandlers != null ? stringhandlers.keySet().stream().toList() : null);
            }

            @Override
            public void getConstraintTypeHandler(ConstraintViolationHandler.ConstraintType t, Z arg) {
                enumhandlers.get(t).accept(arg);
            }

            @Override
            public void getNamedConstraintHandler(String cname, Z arg) {
                stringhandlers.get(cname).accept(arg);
            }
        };
    }

    public ConstraintFilters getConstraintFilters(){
        throw new UnsupportedOperationException("Constraint handling not configured in this holder. Class must be overridden.");
    };

    //TODO uh thigh might need to be renamed, these are methods, not methods that return functions
    public void getConstraintTypeHandler(ConstraintViolationHandler.ConstraintType t, T arg) {
        throw new UnsupportedOperationException("Constraint handling not configured in this holder. Class must be overridden.");
    };

    //TODO uh thigh might need to be renamed, these are methods, not methods that return functions
    public void getNamedConstraintHandler(String cname, T arg) {
        throw new UnsupportedOperationException("Constraint handling not configured in this holder. Class must be overridden.");
    };
}
