package com.epam.training.ticketservice.core.book;


import com.epam.training.ticketservice.core.user.User;
import com.epam.training.ticketservice.core.user.UserDto;
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
    @ManyToOne
    private User user;
    private String movieName;
    private String roomName;
    private LocalDateTime date;
    @ElementCollection
    @CollectionTable(name = "string_list_table", joinColumns = @JoinColumn(name = "entity_id"))
    private List<String> seats;

    private int BASE_PRICE = 1500;


    //private int price = seats.size() * BASE_PRICE;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public Book(User user, String movieName, String roomName, String dateTimeString, List<String> seats) {
        this.user=user;
        this.movieName = movieName;
        this.roomName = roomName;
        this.date = LocalDateTime.parse(dateTimeString,formatter);
        this.seats = seats;
    }
}
