package com.epam.training.ticketservice.lib.screening;

import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder;
import com.epam.training.ticketservice.support.db.constraints.ConstraintViolationHandler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.epam.training.ticketservice.support.db.constraints.ConstraintHandlerHolder.createConstraintHandler;

/**
 * Presentation layer should use the alternate key interface for this service. The ID is for internal use.
 */
@Service
public class ScreeningCrudServiceImpl extends CustomCrudServiceImpl<ScreeningDto, Screening, Long, ScreeningMapper, ScreeningCrudRepository> implements ScreeningCrudService {
    public ScreeningCrudServiceImpl(@NonNull ScreeningCrudRepository repo, @NonNull ScreeningMapper mapper) {
        super(repo, mapper);
    }

    @Override
    public Long keyFromStrings(String... s) {
        return Long.parseLong(s[0]);
    }

    @Override
    public int keyFromStringsSize() {
        return 1;
    }

    @Override
    protected ConstraintHandlerHolder<ScreeningDto> getCreateHandler() {
        return createConstraintHandler(
                Map.of(ConstraintViolationHandler.ConstraintType.PRIMARY_KEY, Exceptions::throwExists), //TODO nonexistent FK violations
                Map.of(
                    "NO_OVERLAP", Exceptions::throwOverlap,
                    "NO_OVERLAP_BREAK", Exceptions::throwOverlapBreak,
                    "FUTURE_ONLY", Exceptions::throwPast)
        );
    }

    @Override
    protected ConstraintHandlerHolder<ScreeningDto> getUpdateHandler() {
        return createConstraintHandler(
            null,
            Map.of(
                    "CHECK_UPDATE_HAS_ROWS", Exceptions::throwNotExist
            ));
    }

    // use the alternate key interface
    @Override
    @Deprecated
    public void delete(@NonNull Long aLong) {
        throw new UnsupportedOperationException();
    }

    // use the alternate key interface
    @Override
    @Deprecated
    public Optional<ScreeningDto> get(@NonNull Long aLong) {
        throw new UnsupportedOperationException("Screening IDs are database internal.");
    }

    @Override
    public Optional<ScreeningDto> getByAlternateKey(BaseScreening baseScreening) {
        return repo.getByAlternateKey(Screening.class, baseScreening).map(mapper::entityToDto);
    }

    @Override
    public void deleteByAlternateKey(BaseScreening baseScreening) { //TODO maybe I should have made the id the alternate key
        var screening = repo.getByAlternateKey(Screening.class, baseScreening).get();
        // TODO design issue; The exception in the net line should never be called because that means the above line should have already thrown...
        //super.rawDelete(screening.getScreeningId(), (aLong, e) -> { Exceptions.throwNotExistWithContext("", e); });
        super.rawDelete(screening.getScreeningId(), (aLong, e) -> { throw new UnsupportedOperationException(); });
    }
}
