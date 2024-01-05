package com.epam.training.ticketservice.lib.user;

import com.epam.training.ticketservice.lib.user.model.UserDto;
import com.epam.training.ticketservice.lib.user.model.UserMapper;
import com.epam.training.ticketservice.support.CustomCrudService;

public interface UserAccountCrudService extends CustomCrudService<UserDto, Long, UserMapper>, UserRegistrationService {
}
