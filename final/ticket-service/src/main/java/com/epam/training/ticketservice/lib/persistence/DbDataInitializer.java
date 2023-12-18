package com.epam.training.ticketservice.lib.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.booking.persistence.BookingRepository;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.pricing.persistence.*;
import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationRepository;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningRepository;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import com.epam.training.ticketservice.lib.user.persistence.Admin;
import com.epam.training.ticketservice.lib.user.persistence.AdminRepository;
import com.epam.training.ticketservice.lib.user.persistence.User;
import com.epam.training.ticketservice.lib.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

// TODO https://github.com/spring-projects/spring-framework/issues/31756

@Component
// DependsOn seems to work by causing invocation of dependent beans, so it should work to try to invoke
// the various db init beans that then decide whether they should run or not
//TODO needs a dependency on the database specific db initializers
@DependsOn({"entityManagerFactory"})
@RequiredArgsConstructor
//@RequiredArgsConstructor //TODO tried to do this with ApplicationRunner but run() wouldnt get called for some reason.
@Profile("testdata")
public class DbDataInitializer implements InitializingBean {
    private final RoomCrudRepository ror;
    private final MovieCrudRepository mr;
    private final ScreeningRepository sr;
    private final ReservationRepository rer;
    private final BookingRepository br;
    private final UserRepository ur;
    private final AdminRepository ar;


    private final BasePriceRepository bpr;
    private final SurchargeRepository sur;
    private final MovieSurchargeRepository msr;
    private final RoomSurchargeRepository rsr;
    private final ScreeningSurchargeRepository ssr;

    //TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on this
    @Override
    public void afterPropertiesSet() throws Exception {
        var analf = new Room("Analfa", 5, 5);
        var beta = new Room("Beta", 5, 5);
        ror.saveAll(Arrays.asList(analf, beta));

        var vekt = new Movie("A Vektor", "humor", 60);
        var st = Instant.now();
        mr.save(vekt);

        var sc = new Screening(null, new BaseScreening(analf, vekt, st));
        sr.save(sc);

        //TODO link booking and reservations
        var bk = new Booking(null, sc, 1000);
        br.save(bk);

        var res = new Reservation(new ReservationKey(bk, sc, new Seat(2, 3)));
        rer.save(res);

        var us = new User(null, "user", "pw", Set.of(bk));
        ur.save(us);

        var adm = new Admin(null, "admin", "admin");
        ar.save(adm);

        var bp = new BasePrice(1000);
        bpr.save(bp);

        var ck = new Surcharge("cake", 100);
        sur.saveAll(Set.of(ck, new Surcharge("and eat it too", 1000)));
        msr.save(new MovieSurchargeMap(null, ck, vekt));
        rsr.save(new RoomSurchargeMap(null, ck, beta));
        ssr.save(new ScreeningSurchargeMap(null, ck, sc));
    }
}
