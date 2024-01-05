package com.epam.training.ticketservice.lib.ticket.persistence;

import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * Reservations are handled through the reservations relation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long ticketId;

    @ManyToOne(optional = false)
    public Screening screening;

    @Column(nullable = false)
    //public Currency paid;
    public Integer paid;
}
