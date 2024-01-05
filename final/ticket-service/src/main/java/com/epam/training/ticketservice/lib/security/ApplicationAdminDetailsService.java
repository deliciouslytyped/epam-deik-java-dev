package com.epam.training.ticketservice.lib.security;

import com.epam.training.ticketservice.lib.user.persistence.ApplicationAdmin;
import com.epam.training.ticketservice.lib.user.persistence.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationAdminDetailsService extends SubtypingUserDetailsService<AdminRepository, ApplicationAdmin> implements UserDetailsService {
    protected final AdminRepository ar;

    @Override
    protected AdminRepository getRepository() {
        return ar;
    }

    @Override
    protected String getRole() {
        return Roles.ROLE_ADMIN.toString();
    }
}
