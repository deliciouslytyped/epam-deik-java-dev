package com.epam.training.ticketservice.core.book.persistence;

import com.epam.training.ticketservice.core.book.model.SeatDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "seats")
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer rows;
    private Integer cols;

    public Seat(int row,int col) {
        this.rows = row;
        this.cols = col;
    }

    public Seat(SeatDto seatDto) {
        rows = seatDto.row();
        cols = seatDto.col();
    }
}
