package com.epam.training.ticketservice.presentation.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;


//sign in, sign up, describe accuont, sign in privileged, sign out
@RequiredArgsConstructor
@ShellComponent
public class AccountCommands {
    @Qualifier("adminAuthenticationManager")
    protected final AuthenticationManager adminAuthManager;
    @Qualifier("userAuthenticationManager")
    protected final AuthenticationManager userAuthManager;

    //TODO return type
    protected String subtypedAuth(AuthenticationManager authm, Authentication token){
        try {
            var result = authm.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (BadCredentialsException e) {
            return "Authentication failure";
        }
        return "Authenticated successfully.";
    }
    @ShellMethod(key = "sign in privileged", value="Log in to an admin account.")
    String adminLogin(String username, String password){
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        return subtypedAuth(adminAuthManager, auth);
    }

    @ShellMethod(key = "sign in", value = "Log in to a user account.")
    String userLogin(String username, String password){
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        return subtypedAuth(userAuthManager, auth);
    }

    @ShellMethod(key = "describe account", value = "Show information about the logged in account.")
    String describe(){
        return "";
    }

    @ShellMethod(key = "sign out", value = "Sign out of the currently logged in account.")
    String signOut(){
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Signed out.";
    }
}