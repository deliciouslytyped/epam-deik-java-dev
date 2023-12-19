package com.epam.training.ticketservice.lib.screening;

import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.support.CustomCrudService;
import com.epam.training.ticketservice.support.ServiceByAlternateKey;

public interface ScreeningCrudService extends CustomCrudService<ScreeningDto, Long>, ServiceByAlternateKey<ScreeningDto, BaseScreening> {
}