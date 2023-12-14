package com.epam.training.ticketservice.lib.db.constraints;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

// https://stackoverflow.com/questions/1246803/constraintviolationexception-vs-dataintegrityviolationexception
public class HibernateConstraintMatcher {
    protected ConstraintViolationException toCVE(Exception e) {
        ConstraintViolationException ex;
        if (e instanceof ConstraintViolationException) {
            ex = (ConstraintViolationException) e;
        } else {
            throw new IllegalStateException();
        }
        return ex;
    }


    public boolean isType(Exception e, ConstraintViolationHandler.ConstraintType t){
        if (e instanceof ConstraintViolationException){ //TODO is this ever run?
            return isTypeCVE((ConstraintViolationException)e, t);
        } else if (e instanceof DataIntegrityViolationException && e.getCause() instanceof PersistenceException){//TODO why did stuff appear to work before without this branch?
            return isTypePE((PersistenceException)e.getCause(), t);
        } else {
            throw new UnsupportedOperationException("Unhandled database integrity violation?", e);
        }
    }

    //Anything else that ends up in DataIntegrityViolation ? todo check source
    public boolean isNamed(Exception e, String n){
        if (e instanceof ConstraintViolationException){
            return isNamedCVE((ConstraintViolationException)e, n);
        } else if (e instanceof DataIntegrityViolationException && e.getCause() instanceof ConstraintViolationException){//TODO why did stuff appear to work before without this branch?
            return isNamedPE((ConstraintViolationException)e.getCause(), n);
        } else {
            throw new UnsupportedOperationException("Unhandled database integrity violation?", e);
        }
    }

    public boolean isTypePE(PersistenceException e, ConstraintViolationHandler.ConstraintType t){
        if(e instanceof EntityExistsException && t.equals(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNamedPE(PersistenceException e, String constraintName){
        /*if (e.getConstraintName().startsWith("\"" + constraintName)){
            return true;
        }*/
        if (e instanceof ConstraintViolationException e2 && e2.getConstraintName().startsWith("\"" + constraintName)){
            return true;
        }
        return false; //TODO
    }
    public boolean isTypeCVE(ConstraintViolationException e, ConstraintViolationHandler.ConstraintType t){
        switch(t) { //TODO
            case PRIMARY_KEY -> {
                if (
                        //TODO why the hell arent these real constraint names, why are they some weird f**ing string messages
                        //eg: TODO / is this variant a thing or did I just not check carefully enough?
                        e.getConstraintName().startsWith("[\"PUBLIC.PRIMARY_KEY") || // Hibernate?
                        //eg: "PUBLIC.PRIMARY_KEY_26 ON PUBLIC.ROOM(NAME) VALUES ( /* 3 */ 'wah' )"; SQL statement: insert into room (col_count, row_count, name) values (?, ?, ?) [23505-214]
                        e.getConstraintName().startsWith("\"PUBLIC.PRIMARY_KEY")  // Spring?
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

    public boolean isNamedCVE(ConstraintViolationException e, String constraintName){
        //eg (spring): "CHECK_NAME: "; SQL statement: insert into room (col_count, row_count, name) values (?, ?, ?) [23513-214]
        if (e.getConstraintName().startsWith("\"" + constraintName)){
            return true;
        }
        return false; //TODO
    }

}
