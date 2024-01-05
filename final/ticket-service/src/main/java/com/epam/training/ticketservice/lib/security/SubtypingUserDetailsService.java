package com.epam.training.ticketservice.lib.security;

import com.epam.training.ticketservice.lib.user.persistence.base.UserBase;
import com.epam.training.ticketservice.lib.user.persistence.base.UserBaseFragment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public abstract class SubtypingUserDetailsService<T extends UserBaseFragment<E>, E extends UserBase> implements UserDetailsService {
    protected abstract T getRepository();
    protected abstract String getRole();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var password = getRepository()
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No entity found for username."))
                .getPassword();

        return new User(username, password, List.of(new SimpleGrantedAuthority(getRole())));
    }
}

