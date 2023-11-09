package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.service.AuthenticationService;
import com.epam.training.ticketservice.service.UserService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authManager;
    private final UserService userService;

    @Override
    public Result<?, OperationException> signup(String username, String password) {
        return userService.createUser(username, password);
    }

    @Override
    public Result<?, AuthenticationException> login(String username, String password, boolean privileged) {
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        try {
            var result = authManager.authenticate(auth);
            var hasAdmin = result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (privileged && !hasAdmin || !privileged && hasAdmin)
                throw new BadCredentialsException("huhh?!");
            SecurityContextHolder.getContext().setAuthentication(result);
            return Result.ok(null);
        } catch (AuthenticationException e) {
            return Result.err(e);
        }
    }

    @Override
    public Result<?, AuthenticationException> logout() {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            return Result.ok(null);
        } catch (AuthenticationException e) {
            return Result.err(e);
        }
    }
}
