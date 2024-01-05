package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.user.persistence.base.UserBaseFragment;
import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ApplicationUserCrudRepository extends JpaRepository<ApplicationUser, Long>, UserBaseFragment<ApplicationUser>, UpdateByEntityFragment<ApplicationUser> {
    @Query("select a from ApplicationUser a join fetch a.bookings where a.uid = ?1")
    Optional<ApplicationUser> findByUidWithBookings(@NonNull Long uid);

    //TODO understand this better
    @Query("select a from ApplicationUser a left join fetch a.bookings b left join fetch b.screening")
    List<ApplicationUser> findAllWithBookingsAndScreenings();
}