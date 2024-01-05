package com.epam.training.ticketservice.support;

import com.epam.training.ticketservice.lib.security.aspects.DefaultPrivileged;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import com.epam.training.ticketservice.support.jparepo.CustomJpaRepository;
import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class CustomCrudServiceImpl<T,U,ID,M extends CustomMapper<T,U,ID>,R extends JpaRepository<U,ID> & UpdateByEntityFragment<U>> implements CustomCrudService<T,ID,M> {
    protected final R repo;
    public final M mapper;

    public M getMapper(){
        return mapper;
    }

    public abstract ID keyFromStrings(String... s); //TODO wish I didnt have to override this in every implementor but I couldnt figure out any way to get it to work. it causes implicit type conversion ambiguity errors if i try to do it in the mapstruct / mapping classes, which is where this *should* live...
    public abstract int keyFromStringsSize();

    protected ConstraintHandlerHolder<T> getCreateHandler() { return new ConstraintHandlerHolder<T>(); };
    protected ConstraintHandlerHolder<T> getUpdateHandler() { return new ConstraintHandlerHolder<T>(); };
    protected ConstraintHandlerHolder<ID> getDeleteHandler() { return new ConstraintHandlerHolder<ID>(); };

    protected <Z> void useHandler(Supplier<ConstraintHandlerHolder<Z>> getHH, Z arg, Runnable r) {
        var handler = getHH.get();
        new ConstraintViolationHandler(r).lazyHandlerRun(handler, arg);
    }

    //TODO move to security and inject?
    @Override
    public void create(@NonNull T entityDto) {
        useHandler(this::getCreateHandler, entityDto, () -> { //Not using simple try catch for this because hibernate makes it a pain to access the violation type? so im trying to clean up the business logic like this?
            repo.save(mapper.dtoToEntity(entityDto));
        });
    }

    //TODO if not exist?
    //TODO default implementation is same as create...? / note! we use a modified hibernate context such that save doesnt update, only create
    @Override
    public void update(@NonNull T entityDto) {
        useHandler(this::getUpdateHandler, entityDto, () -> {
            repo.update(mapper.dtoToEntity(entityDto));
        });
    }

    //TODO arguably these should take the entire DTO so they can be passed to the handlers,
    // but the reflection to get the ID attribute is messy so instead we have the caller get the ID and pass it instead, for now.
    @Override
    public void delete(@NonNull ID id) {
        useHandler(this::getDeleteHandler, id, () -> { //TODO I really hope using the wrong handler here by copy-paste accident wasnt the problem
            repo.deleteById(id);
        });
    }

    //TODO we dont get the exception for trying to delete a missing item because hibernate does a lookup first and its too difficult to override, so just use the exception hibernate returns
    //TODO I'm putting this here so subclasses can override delete and call it
    protected void rawDelete(@NonNull ID s, BiConsumer<ID,Exception> exHandler) { //The second parameter here is a bit of a hack
        try {
            repo.deleteById(s);
        } catch (EmptyResultDataAccessException e) {
            exHandler.accept(s, e);
        }
    }

    //These are just queries, so there shouldn't be any constraint violatin going on here, and we don't need the handlers?
    @Override
    public Optional<T> get(@NonNull ID id) {
        return repo.findById(id).map(mapper::entityToDto);
    }

    @Override
    public List<T> list() {
        return repo.findAll().stream().map(mapper::entityToDto).toList();
    }

}