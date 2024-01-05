package com.epam.training.ticketservice.lib.security;

import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService extends SubtypingUserDetailsService<ApplicationUserCrudRepository, ApplicationUser> implements UserDetailsService {
    protected final ApplicationUserCrudRepository ur;

    @Override
    protected ApplicationUserCrudRepository getRepository() {
        return ur;
    }

    @Override
    protected String getRole() {
        return Roles.ROLE_USER.toString();
    }
}
