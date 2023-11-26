package com.epam.training.ticketservice.core.user.model;

import com.epam.training.ticketservice.core.user.persistence.Role;

public record UserDto(String username, Role role) {
}
