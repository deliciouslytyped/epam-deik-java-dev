package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.model.UserRole;
import lombok.Getter;

@Getter
public class UserDto {
    private final String username;
    private final UserRole role;

    public UserDto(User dao) {
        this.username = dao.getUsername();
        this.role = dao.getRole();
    }
}
