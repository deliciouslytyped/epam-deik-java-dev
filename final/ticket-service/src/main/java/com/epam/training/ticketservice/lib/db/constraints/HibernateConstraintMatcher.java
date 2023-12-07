package com.epam.training.ticketservice.lib.db.constraints;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class HibernateConstraintMatcher {
    protected ConstraintViolationException toCVE(Exception e) {
        ConstraintViolationException ex;
        if (e instanceof DataIntegrityViolationException){
            ex = (ConstraintViolationException) e.getCause();
        }
        else if (e instanceof ConstraintViolationException) {
            ex = (ConstraintViolationException) e;
        } else {
            throw new IllegalStateException();
        }
        return ex;
    }
    protected boolean isType(Exception e, ConstraintViolationHandler.ConstraintType t){
        ConstraintViolationException ex = toCVE(e);

        switch(t) { //TODO
            case PRIMARY_KEY -> {
                if (
                        //TODO why the hell arent these real constraint names, why are they some weird f**ing string messages
                        //eg: TODO / is this variant a thing or did I just not check carefully enough?
                        ex.getConstraintName().startsWith("[\"PUBLIC.PRIMARY_KEY") || // Hibernate?
                        //eg: "PUBLIC.PRIMARY_KEY_26 ON PUBLIC.ROOM(NAME) VALUES ( /* 3 */ 'wah' )"; SQL statement: insert into room (col_count, row_count, name) values (?, ?, ?) [23505-214]
                        ex.getConstraintName().startsWith("\"PUBLIC.PRIMARY_KEY")  // Spring?
                ) {
                    return true;
                }
            }
            case ANY -> {
                return true;
            }
        }
        return false;
    }

    protected boolean isNamed(Exception e, String constraintName){
        ConstraintViolationException ex = toCVE(e);
        //eg (spring): "CHECK_NAME: "; SQL statement: insert into room (col_count, row_count, name) values (?, ?, ?) [23513-214]
        if (ex.getConstraintName().startsWith("\"" + constraintName)){
            return true;
        }
        return false; //TODO
    }

}
