package com.epam.training.ticketservice.lib.db.constraints;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

//TODO I guess needing to recreate this every call is inefficient?
//Does the compiler optimize out passing lambdas repeatedly? / inlining?
@RequiredArgsConstructor
public class ConstraintViolationHandler implements Runnable {
    protected static final HibernateConstraintMatcher matcher = new HibernateConstraintMatcher();

    // Yes you can use it even without any handlers set.
    @Override
    public void run() {
        callback.run();
    }

    //TODO i.e. does this make sense?
    //TODO are these unique per table such that they warrant an enum or do I always need to use named constraints?
    public enum ConstraintType { ANY, PRIMARY_KEY };

    private final Runnable callback;

    //Technically you can specify both constrainttype and constraintname but the public interface doesn't do it right now.
    protected <T> boolean handledException(ConstraintType t, String constraintName, Exception e, Runnable r){
        if ((t == null || matcher.isType(e, t)) &&
                (constraintName == null || matcher.isNamed(e, constraintName))) {
            r.run();
            return true;
        }
        return false;
    }

    protected ConstraintViolationHandler build(ConstraintType t, String constraintName, Runnable handler){
        return new ConstraintViolationHandler(() -> {
            try {
                callback.run();
            // TODO so...the CVE gets wrapped and becomes a DIVE, so we cant catch the CVE? Does the CVE ever come directly here or do I not need that branch?
            } catch (ConstraintViolationException | DataIntegrityViolationException e) {
                if (!handledException(t, constraintName, e, handler)) {
                    throw e;
                }
            }
        });
    }

    public ConstraintViolationHandler on(@NonNull ConstraintType t, @NonNull Runnable handler){
        return build(t, null, handler);
    }

    public ConstraintViolationHandler on(@NonNull String constraintName, @NonNull Runnable handler){
        return build(null, constraintName, handler);
    }

    protected <T> LinkedList<T> makeMut(List<T> l){
        LinkedList<T> mut;
        if(! (l instanceof LinkedList)){
            mut = new LinkedList<T>(l);
        } else {
            mut = (LinkedList<T>) l;
        }
        return mut;
    }

    //TODO something something passing exceptions onwards in the chain might not work here?
    public ConstraintViolationHandler on(@NonNull List<String> constraintNames, @NonNull Consumer<String> handler){
        LinkedList<String> mutNames = makeMut(constraintNames);
        var cname = mutNames.remove(0);
        var cvh = build(null, cname, () -> {
            handler.accept(cname);
        });
        if (!mutNames.isEmpty()) {
            return cvh.on(mutNames, handler);
        } else {
            return cvh;
        }
    }
}