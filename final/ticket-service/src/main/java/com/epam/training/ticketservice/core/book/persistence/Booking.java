package com.epam.training.ticketservice.core.book.persistence;


import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.user.persistence.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Screening screening;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> seats;
    private int price;

    public Booking(User user, Screening screening, List<Seat> seats, int price) {
        this.user = user;
        this.screening = screening;
        this.seats = seats;
        this.price = price;
    }
}
