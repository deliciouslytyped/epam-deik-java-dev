package com.epam.training.ticketservice.presentation.cli;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Util {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static Instant parseTime(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant;
    }

    public static Object formatInstant(Instant instant) {
            return formatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }
}
