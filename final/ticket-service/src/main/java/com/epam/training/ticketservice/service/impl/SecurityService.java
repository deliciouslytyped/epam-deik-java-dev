package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.model.UserRole;
import com.epam.training.ticketservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.epam.training.ticketservice.model.User> opt = repository.findByUsername(username);
        if (opt.isEmpty()) throw new UsernameNotFoundException("User does not exist");
        var user = opt.get();
        var builder = User.withUsername(username).password(encoder.encode(user.getPassword()));

        if (user.getRole() == UserRole.ADMIN)
            builder.roles("USER", "ADMIN");
        else if (user.getRole() == UserRole.USER)
            builder.roles("USER");

        return builder.build();
    }
}
