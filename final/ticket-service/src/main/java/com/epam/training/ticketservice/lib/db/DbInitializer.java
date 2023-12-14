package com.epam.training.ticketservice.lib.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

// TODO https://github.com/spring-projects/spring-framework/issues/31756

@Component()
@DependsOn("entityManagerFactory")
@RequiredArgsConstructor
//@RequiredArgsConstructor //TODO tried to do this with ApplicationRunner but run() wouldnt get called for some reason.
public class DbInitializer implements InitializingBean {
    /*
    //TODO moving this mess here:
        //TODO Im not sure this constraint is actually constructed correctly, RE: the RESERVATION. qualified names in the end row.
    @AttributeOverrides({
        @AttributeOverride(name = "seat.rowIdx", column = @Column(
            columnDefinition = "integer check ((0 < COL_IDX) AND (COL_IDX <= " +
                    "(SELECT R.COL_COUNT FROM SCREENING S INNER JOIN ROOM R ON S.ROOM_NAME = R.NAME WHERE SCREENING_SCREENING_ID = S.SCREENING_ID)))"))
    })
    */
    private final EntityManager em;
    private final PlatformTransactionManager tm;

    //TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on this
    @Override
    public void afterPropertiesSet() throws Exception {
        var tt = new TransactionTemplate(tm);
        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("alter table RESERVATION\n" +
                    "    add constraint CHECK_COL\n" +
                    "        check (((0 < COL_IDX) AND (COL_IDX <= (SELECT R.COL_COUNT\n" +
                    "                                               FROM SCREENING S\n" +
                    "                                                        INNER JOIN ROOM R ON S.ROOM_NAME = R.NAME\n" +
                    "                                               WHERE SCREENING_SCREENING_ID = S.SCREENING_ID))));").executeUpdate();
        });

        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("alter table RESERVATION\n" +
                    "    add constraint CHECK_ROW\n" +
                    "        check (((0 < ROW_IDX) AND (ROW_IDX <= (SELECT R.ROW_COUNT\n" +
                    "                                               FROM SCREENING S\n" +
                    "                                                        INNER JOIN ROOM R ON S.ROOM_NAME = R.NAME\n" +
                    "                                               WHERE SCREENING_SCREENING_ID = S.SCREENING_ID))));").executeUpdate();
        });

        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("alter table ROOM add constraint CHECK_ROW_COUNT check (ROW_COUNT > 0);").executeUpdate();
        });
        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("alter table ROOM add constraint CHECK_COL_COUNT check (COL_COUNT > 0);").executeUpdate();
        });

        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("alter table MOVIE add constraint CHECK_RUN_TIME check (RUNTIME > 0);").executeUpdate();
        });
    }
}
