package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.user.persistence.base.UserBaseFragment;
import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//TODO sould this be using customjparepository?
//TODO are the left joins needed?
public interface ApplicationUserCrudRepository extends JpaRepository<ApplicationUser, Long>, UserBaseFragment<ApplicationUser>, UpdateByEntityFragment<ApplicationUser> {
    @Transactional
    @Query("select a from ApplicationUser a left join fetch a.bookings where a.uid = ?1")
    Optional<ApplicationUser> findByUidWithBookings(@NonNull Long uid);

    @Transactional
    @Query("select a from ApplicationUser a left join fetch a.bookings where a.username = ?1")
    Optional<ApplicationUser> findByUsernameWithBookings(@NonNull String uname);

    //TODO understand this better
    @Transactional
    @Query("select a from ApplicationUser a left join fetch a.bookings b left join fetch b.screening")
    List<ApplicationUser> findAllWithBookingsAndScreenings();
}