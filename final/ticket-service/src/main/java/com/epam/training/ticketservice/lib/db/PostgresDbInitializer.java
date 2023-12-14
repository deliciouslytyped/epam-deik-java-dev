package com.epam.training.ticketservice.lib.db;

import com.epam.training.ticketservice.lib.booking.persistence.BookingRepository;
import com.epam.training.ticketservice.lib.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.lib.pricing.persistence.*;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationRepository;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.room.persistence.RoomRepository;
import com.epam.training.ticketservice.lib.screening.persistence.ScreeningRepository;
import com.epam.training.ticketservice.lib.user.persistence.AdminRepository;
import com.epam.training.ticketservice.lib.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.Arrays;

@Component
@DependsOn("entityManagerFactory")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix="spring.jpa", name="database-platform", havingValue="org.hibernate.dialect.PostgreSQLDialect") //TODO check at least one of these isloaded
public class PostgresDbInitializer implements InitializingBean {
    private final EntityManager em;
    private final PlatformTransactionManager tm;

    //TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on this
    @Override
    public void afterPropertiesSet() throws Exception {
        var tt = new TransactionTemplate(tm);
        tt.executeWithoutResult(transactionStatus -> {
            //em.createNativeQuery().executeUpdate();
        });
    }
}
