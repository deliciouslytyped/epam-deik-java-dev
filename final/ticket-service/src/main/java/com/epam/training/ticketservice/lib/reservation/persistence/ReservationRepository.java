package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, ReservationKey> {
}