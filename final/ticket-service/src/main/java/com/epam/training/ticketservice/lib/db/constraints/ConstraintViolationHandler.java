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

    // Allows avoiding implementation of a constraint config holder for cases where we dont want/need to handle constraints,
    // but there is common code (in a superclass) that wants to handle constraints and wraps everything in a constraint handler.
    // This may arise if an abstract superclass has multiple holders that may not need to be used in every case in every subclass. (For example customcrudserviceimpl)
    //
    // This means that when an exception is caught it doesnt immediately fire an unrelated UnsupportedOperationException on
    //  invocation of an unoverridden (unimplemented) holder.
    //TODO/NOTE in most desired cases this probably only works as desired if its the first in the method chain
    public <T> void lazyHandlerRun(ConstraintHandlerHolder<T> h, T arg){
        try {
            callback.run();
        } catch (Exception e) {
            var filters = h.getConstraintFilters();
            var handler = new ConstraintViolationHandler(() -> {
                throw e;
            });
            if (!(filters.enums == null || filters.enums.isEmpty())){
                handler = handler.on(filters.enums, (ConstraintViolationHandler.ConstraintType t) -> h.getConstraintTypeHandler(t, arg));
            }
            if (!(filters.cnames == null || filters.cnames.isEmpty())){
                handler = handler.on(filters.cnames, (String cname) -> h.getNamedConstraintHandler(cname, arg));
            }
            handler.run();
        }
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
    //TODO I really dont like this, is there some way to use a common base class for the first argument in a decent way?
    //TODO this interface should probably be totally refactored to use a Map instead
    public <T> ConstraintViolationHandler on(@NonNull List<T> constraintSpec, @NonNull Consumer<T> handler){
        LinkedList<T> mutNames = makeMut(constraintSpec);
        var c = mutNames.remove(0);
        ConstraintViolationHandler cvh;
        if (c instanceof String) {
            cvh = build(null, (String)c, () -> {
                handler.accept(c);
            });
        }
        else if (c instanceof ConstraintType) {
            cvh = build((ConstraintType)c, null, () -> {
                handler.accept(c);
            });
        }
        else { throw new UnsupportedOperationException("Unexpected constraint type for ConstraintViolationHandler."); };
        if (!mutNames.isEmpty()) {
            return cvh.on(mutNames, handler);
        } else {
            return cvh;
        }
    }
}