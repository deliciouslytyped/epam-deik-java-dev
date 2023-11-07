package com.epam.training.ticketservice.core.book;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue
    private Integer id;
    private String movieName;
    private String roomName;
    private LocalDateTime date;
    @ElementCollection
    @CollectionTable(name = "string_list_table", joinColumns = @JoinColumn(name = "entity_id"))
    private List<Integer> seats;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public Book(String movieName, String roomName, String dateTimeString, List<Integer> seats) {
        this.movieName = movieName;
        this.roomName = roomName;
        this.date = LocalDateTime.parse(dateTimeString,formatter);
        this.seats = seats;
    }
}
