package com.epam.training.ticketservice.lib.persistence;

import com.epam.training.ticketservice.lib.ticket.persistence.Ticket;
import com.epam.training.ticketservice.lib.ticket.persistence.TicketCrudRepository;
import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.movie.persistence.MovieCrudRepository;
import com.epam.training.ticketservice.lib.pricing.persistence.*;
import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationCrudRepository;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomCrudRepository;
import com.epam.training.ticketservice.lib.screening.persistence.BaseScreening;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningCrudRepository;
import com.epam.training.ticketservice.lib.reservation.persistence.Seat;
import com.epam.training.ticketservice.lib.user.persistence.AdminRepository;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUserCrudRepository;
import com.epam.training.ticketservice.lib.user.persistence.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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
//@ConditionalOnProperty(prefix="spring.jpa.hibernate", name="ddl-auto", havingValue = "create-drop") //TODO figure out how to do this properly
public class DbDataInitializer implements InitializingBean {
    private final RoomCrudRepository ror;
    private final MovieCrudRepository mr;
    private final ScreeningCrudRepository sr;
    private final ReservationCrudRepository rer;
    private final TicketCrudRepository br;
    private final ApplicationUserCrudRepository ur;
    private final AdminRepository ar;


    private final BasePriceRepository bpr;
    private final SurchargeRepository sur;
    private final MovieSurchargeRepository msr;
    private final RoomSurchargeRepository rsr;
    private final ScreeningSurchargeRepository ssr;

    //TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on this
    @Override
    public void afterPropertiesSet() throws Exception {
        //"check" if already loaded //TODO
        if(ror.findById("Analfa").isPresent()){
            System.out.println(" initial data may already be present. not adding.");
            return;
        }

        var dpe = PasswordEncoderFactories.createDelegatingPasswordEncoder(); //TODO Ok this raises a point in that if I dont use the standard stuff I have to do this manually

        var analf = new Room("Analfa", 5, 5);
        var beta = new Room("Beta", 5, 5);
        ror.saveAll(Arrays.asList(analf, beta));

        var vekt = new Movie("A Vektor", "humor", 60);
        var st = Instant.now().plusSeconds(3600*24*300); // Will violate future constraint otherwise
        mr.save(vekt);

        var sc = new Screening(null, new BaseScreening(analf, vekt, st));
        sr.save(sc);

        //TODO link booking and reservations
        var bk = new Ticket(null, sc, 1000);
        br.save(bk);

        var res = new Reservation(new ReservationKey(bk, sc, new Seat(2, 3)));
        rer.save(res);

        var us = new ApplicationUser(null, "user", dpe.encode("pw"), Set.of(bk));
        ur.save(us);

        /*var adm = new Admin(null, "admin", "admin");
        ar.save(adm);*/
        var usr = new ApplicationUser(null, "steve", dpe.encode("pls"), null);
        ur.save(usr);

        var bp = new BasePrice(1000);
        bpr.save(bp);

        var ck = new Surcharge("cake", 100);
        sur.saveAll(Set.of(ck, new Surcharge("and eat it too", 1000)));
        msr.save(new MovieSurchargeMap(null, ck, vekt));
        rsr.save(new RoomSurchargeMap(null, ck, beta));
        ssr.save(new ScreeningSurchargeMap(null, ck, sc));
    }
}
