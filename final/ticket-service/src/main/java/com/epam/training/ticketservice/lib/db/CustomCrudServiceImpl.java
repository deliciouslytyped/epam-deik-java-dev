package com.epam.training.ticketservice.lib.db;

import com.epam.training.ticketservice.lib.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.lib.db.constraints.ConstraintViolationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.function.Supplier;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

public abstract class CustomCrudServiceImpl<T,U,ID,R extends JpaRepository<U,ID> & UpdateByEntity<U>> implements CustomCrudService<T, U,ID, R> {
    protected @NonNull R repo; //TODO No final because abstract, need to force init somehow?

    protected abstract U DTOtoEntity(T entityDto); //TODO bijection object //TODO? out of the two operations this is bijectionally unsafe
    protected abstract T EntityToDto(U entity);
    //TODO these are too flexible or something and their overrides get some kind of unchecked type warning
    protected <Z> ConstraintHandlerHolder<Z> getCreateHandler() { return new ConstraintHandlerHolder<Z>(); };
    protected <Z> ConstraintHandlerHolder<Z> getGetHandler() { return new ConstraintHandlerHolder<Z>(); };
    protected <Z> ConstraintHandlerHolder<Z> getUpdateHandler() { return new ConstraintHandlerHolder<Z>(); };
    protected <Z> ConstraintHandlerHolder<Z> getDeleteHandler() { return new ConstraintHandlerHolder<Z>(); };
    protected <Z> ConstraintHandlerHolder<Z> getListHandler() { return new ConstraintHandlerHolder<Z>(); };

    protected <Z> void useHandler(Supplier<ConstraintHandlerHolder<Z>> getHH, Z arg, Runnable r) {
        var handler = getHH.get();
        new ConstraintViolationHandler(r).lazyHandlerRun(handler, arg);
    }
    @Override
    public void create(@NonNull T entityDto) {
        useHandler(this::getCreateHandler, entityDto, () -> { //Not using simple try catch for this because hibernate makes it a pain to access the violation type? so im trying to clean up the business logic like this?
            repo.save(DTOtoEntity(entityDto));
        });
    }

    //TODO if not exist?
    //TODO default implementation is same as create...? / note! we use a modified hibernate context such that save doesnt update, only create
    @Override
    public void update(@NonNull T entityDto) {
        useHandler(this::getUpdateHandler, entityDto, () -> {
            repo.update(DTOtoEntity(entityDto));
        });
    }

    @Override
    public void delete(@NonNull ID id) {
        useHandler(this::getUpdateHandler, id, () -> {
            repo.deleteById(id);
        });
    }

    @Override
    public Optional<T> get(@NonNull ID id) {
        return repo.findById(id).map(this::EntityToDto);
    }

    @Override
    public List<T> list() {
        return repo.findAll().stream().map(this::EntityToDto).toList();
    }
}