package com.epam.training.ticketservice.lib.booking.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}