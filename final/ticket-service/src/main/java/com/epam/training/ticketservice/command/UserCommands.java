package com.epam.training.ticketservice.command;

import com.epam.training.ticketservice.model.UserRole;
import com.epam.training.ticketservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class UserCommands extends PrivilegedCommands {
    private final UserService service;

    @ShellMethod(key = "describe account", value = "blah blah")
    public String describe() {
        var res = service.getUser(SecurityContextHolder.getContext().getAuthentication());
        if (res.isOk()) {
            var dto = res.result();
            var sb = new StringBuilder();
            if (dto.getRole() == UserRole.ADMIN) {
                sb.append("Signed in with privileged account '").append(dto.getUsername()).append("'\n");
            } else {
                sb.append("Signed in with account '").append(dto.getUsername()).append("'\n");
            }
            if (dto.getBookings().isEmpty()) {
                sb.append("You have not booked any tickets yet");
            } else {
                sb.append("Your previous bookings are\n");
                dto.getBookings().forEach(b -> sb.append(b).append("\n"));
            }
            return sb.toString();
        }
        return "You are not signed in";
    }
}
