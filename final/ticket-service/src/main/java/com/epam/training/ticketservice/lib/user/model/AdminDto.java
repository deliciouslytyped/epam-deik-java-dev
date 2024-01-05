package com.epam.training.ticketservice.lib.user.model;

import lombok.Data;

@Data
public class AdminDto {// TODO these should probly be extending a common base class
    private final Long uid;
    private final String uname;
}
