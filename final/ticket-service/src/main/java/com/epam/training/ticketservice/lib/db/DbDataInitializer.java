package com.epam.training.ticketservice.lib.db;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.booking.persistence.BookingRepository;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.lib.pricing.persistence.*;
import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationRepository;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomRepository;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

// TODO https://github.com/spring-projects/spring-framework/issues/31756

@Component
@DependsOn({"entityManagerFactory", "dbInitializer"})
@RequiredArgsConstructor
//@RequiredArgsConstructor //TODO tried to do this with ApplicationRunner but run() wouldnt get called for some reason.
@Profile("testdata")
public class DbDataInitializer implements InitializingBean {
    private final RoomRepository ror;
    private final MovieRepository mr;
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
