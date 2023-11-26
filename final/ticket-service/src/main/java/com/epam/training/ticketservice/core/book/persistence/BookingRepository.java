package com.epam.training.ticketservice.core.book.persistence;

import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.user.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    List<Booking> findAllByScreening(Screening screening);

    List<Booking> findAllByUser(User user);



}
