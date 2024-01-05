package com.epam.training.ticketservice.lib.user;

import com.epam.training.ticketservice.lib.user.model.AdminDto;
import com.epam.training.ticketservice.lib.user.model.AdminMapper;
import com.epam.training.ticketservice.support.CustomCrudService;

public interface AdminAccountCrudService extends CustomCrudService<AdminDto, Long, AdminMapper>, UserRegistrationService {
}
