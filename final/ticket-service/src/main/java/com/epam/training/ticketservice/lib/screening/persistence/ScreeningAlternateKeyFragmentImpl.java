package com.epam.training.ticketservice.lib.screening.persistence;

import com.epam.training.ticketservice.support.jparepo.AlternateKeyFragmentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.EntityInformation;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public class ScreeningAlternateKeyFragmentImpl extends AlternateKeyFragmentImpl<Screening,BaseScreening,Long> implements ScreeningAlternateKeyFragment {
    @Override
    public Function<Root<Screening>, Path<BaseScreening>> getAlternateKey() {
        return screeningRoot -> screeningRoot.get("screening");
    }
}
