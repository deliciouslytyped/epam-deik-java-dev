package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDTO {
    private final String username;
    private final UserRole role;
}
