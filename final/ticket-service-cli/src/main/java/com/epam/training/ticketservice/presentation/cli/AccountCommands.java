package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.user.UserAccountCrudService;
import com.epam.training.ticketservice.lib.user.UserAccountCrudServiceImpl;
import com.epam.training.ticketservice.lib.user.model.UserCreationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;


//sign in, sign up, describe accuont, sign in privileged, sign out
@RequiredArgsConstructor
@ShellComponent
public class AccountCommands implements PrivilegedCommand {
    @Qualifier("adminAuthenticationManager")
    protected final AuthenticationManager adminAuthManager;
    @Qualifier("userAuthenticationManager")
    protected final AuthenticationManager userAuthManager;

    protected final UserAccountCrudService us;

    //TODO return type
    protected String subtypedAuth(AuthenticationManager authm, Authentication token){
        try {
            var result = authm.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (BadCredentialsException e) {
            return "Authentication failure.";
        }
        return "Authenticated successfully.";
    }
    @ShellMethod(key = "sign in privileged", value="Log in to an admin account.")
    String adminLogin(String username, String password){
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        var res = subtypedAuth(adminAuthManager, auth);
        return checkAuth(res);
    }

    @ShellMethod(key = "sign in", value = "Log in to a user account.")
    String userLogin(String username, String password){
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        var res = subtypedAuth(userAuthManager, auth);
        return checkAuth(res);
    }

    // Translation for tests
    private String checkAuth(String res) {
        if (res.equals("Authentication failure.")) {
            return "Login failed due to incorrect credentials";
        } else if (res.equals("Authenticated successfully.")) {
            return res;
        } else {
            throw new IllegalStateException();
        }
    }

    @ShellMethod(key = "describe account", value = "Show information about the logged in account.")
    String describe(){
        var res = new StringBuilder();
        var auth = SecurityContextHolder.getContext().getAuthentication(); //TODO may be null if unauthenticated
        String uname;
        if (auth != null && auth.isAuthenticated()) {
            uname = PrivilegedCommand.getUsername(auth);
            var isPrivileged = PrivilegedCommand.isPrivileged(auth);
            res.append(FMT."Signed in with \{isPrivileged ? "privileged " : ""}account '\{uname}'");
            res.append("\n");
            if(isPrivileged) { return res.toString(); }
        } else {
            res.append("You are not signed in");
            res.append("\n");
            return res.toString();
        }

        //TODO hack
        var bookings = ((UserAccountCrudServiceImpl)us)
                .findByUsernameWithBookings(uname)
                .filter(userDto -> !userDto.getBookings().isEmpty());
        if(bookings.isEmpty()) {
            res.append("You have not booked any tickets yet");
        } else {
            res.append("Your previous bookings are\n");
            bookings.get().getBookings().forEach(ticket -> {
                var seats = ticket.getReservations().stream()
                        .map(reservationDto -> {
                            var seat = reservationDto.getSeat();
                            return FMT."(\{seat.getRowIdx()},\{seat.getColIdx()})";
                        })
                        .collect(Collectors.joining(", "));
                res.append(FMT."Seats \{seats} on \{ticket.getScreening().getMovieTitle()} in room \{ticket.getScreening().getRoomName()} starting at \{Util.formatInstant(ticket.getScreening().getTime())} for \{ticket.getPaid()} HUF");
            });
        }

        return res.toString();
    }

    @ShellMethod(key = "sign up", value = "Create a new user account.")
    String signUp(String uname, String pw){
        us.register(new UserCreationDto(uname, pw));
        return null;
    }

    @ShellMethod(key = "sign out", value = "Sign out of the currently logged in account.")
    String signOut(){
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Signed out.";
    }
}