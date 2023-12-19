package com.epam.training.ticketservice.lib.screening;

import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningCrudRepository;
import com.epam.training.ticketservice.support.CustomCrudServiceImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Presentation layer should use the alternate key interface for this service. The ID is for internal use.
 */
@Service
public class ScreeningCrudServiceImpl extends CustomCrudServiceImpl<ScreeningDto, Screening, Long, ScreeningMapper, ScreeningCrudRepository> implements ScreeningCrudService {
    public ScreeningCrudServiceImpl(@NonNull ScreeningCrudRepository repo, @NonNull ScreeningMapper mapper) {
        super(repo, mapper);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ScreeningDto> getByAlternateKey(BaseScreening baseScreening) {
        return repo.getByAlternateKey(baseScreening).map(mapper::entityToDto);
    }

    @Override
    public void deleteByAlternateKey(BaseScreening baseScreening) { //TODO maybe I should have made the id the alternate key
        var screening = repo.getByAlternateKey(baseScreening).get();
        super.rawDelete(screening.getScreeningId(), (aLong, e) -> { throw new UnsupportedOperationException(); });
    }
}
