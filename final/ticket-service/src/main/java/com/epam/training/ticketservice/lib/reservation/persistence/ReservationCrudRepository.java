package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ReservationCrudRepository extends JpaRepository<Reservation, ReservationKey>, UpdateByEntityFragment<Reservation> {
    @Query("select r from Reservation r where r.reservationFor.booking.ticketId = ?1")
    List<Reservation> findByReservationFor_Booking_TicketId(@NonNull Long ticketId);
}