package com.epam.training.ticketservice.lib.persistence;
//This is largely unnecessary but I wanted to try stored procedures
// TODO https://github.com/spring-projects/spring-framework/issues/31756
//TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on  afterPropertiesSet lifecycle
import com.epam.training.ticketservice.support.db.storedprocedures.H2MockTime;
import com.epam.training.ticketservice.support.db.storedprocedures.H2StoredProcedures;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import static org.h2.message.DbException.getJdbcSQLException;

@Component
@DependsOn("entityManagerFactory")
@RequiredArgsConstructor
//TODO check at least one of these is loaded
@ConditionalOnProperty(prefix="spring.jpa", name="database-platform", havingValue="org.hibernate.dialect.H2Dialect")
public class H2DbInitializer implements InitializingBean {
    private final EntityManager em;
    private final PlatformTransactionManager tm;
    private TransactionTemplate tt;

    @Override
    public void afterPropertiesSet() {
        tt = new TransactionTemplate(tm); // Need a transaction to be able to execute the query

        initMockTime();
        // These operations should be idempotent
        addStoredProcedures();
        //addSequences();
        setSequences();
        initRequiredData();

    }

    public void initMockTime(){
        tt.executeWithoutResult(transactionStatus -> {
            // TODO Creating aliases commits the active transaction so we cant scope this to individual tests without touching the test transaction infra

            // TODO we cant drop it if SCREENING and its check constraint exist in the database because the check constraint depends on it??
            //  shouldnt / could this not be an issue given that dropping it should result in remapping to the existing current_timestamp function?
            // TODO temporarily solved by using create if not exists but not refreshing the db will probably eventually run into problems, if not for this specifically...
            /*em.createNativeQuery("DROP ALIAS IF EXISTS \"CURRENT_TIMESTAMP\"")
                    .executeUpdate();*/
            em.createNativeQuery("CREATE ALIAS IF NOT EXISTS CURRENT_TIMESTAMP FOR '"
                            + H2MockTime.class.getCanonicalName() +".getMockTime'")
                    .executeUpdate();
        });
    }



    public void addStoredProcedures() {
        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("DROP ALIAS IF EXISTS nonempty_update_by_key")
                    .executeUpdate();
            em.createNativeQuery("CREATE ALIAS nonempty_update_by_key FOR '"
                            + H2StoredProcedures.class.getCanonicalName() +".nonempty_update_by_key'")
                    .executeUpdate();
        });
    }

    /*public void addSequences(){ //TODO is it possible to make these hibernate created/dropped even thoug hibernate doesnt understand/whatever what they are?
        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("CREATE SEQUENCE IF NOT EXISTS application_uid")
                    .executeUpdate();
            em.createNativeQuery("ALTER TABLE ADMIN ALTER COLUMN UID SET DEFAULT NEXT VALUE FOR application_uid")
                    .executeUpdate();
        });
    }*/

    public void setSequences(){
    tt.executeWithoutResult(transactionStatus -> {
        em.createNativeQuery("ALTER TABLE APP_ADMIN ALTER COLUMN UID SET DEFAULT NEXT VALUE FOR application_uid_seq")
                .executeUpdate();
        em.createNativeQuery("ALTER TABLE APP_USER ALTER COLUMN UID SET DEFAULT NEXT VALUE FOR application_uid_seq")
                .executeUpdate();
        });
    }

    public void initRequiredData() { //TODO maybe this should be inthe security section or something?
        var dpe = PasswordEncoderFactories.createDelegatingPasswordEncoder(); //TODO Ok this raises a point in that if I dont use the standard stuff I have to do this manually
        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery(
                    "MERGE INTO APP_ADMIN\n" +
                            "    USING (SELECT 'admin' uname FROM DUAL) \n" +
                            "    ON username = uname \n" +
                            "    WHEN NOT MATCHED THEN\n" +
                            "        INSERT (USERNAME, PASSWORD) VALUES (?1 , ?2)")
                    .setParameter(1, "admin")
                    .setParameter(2, dpe.encode("adm"))
                    .executeUpdate();
        });
    }
}
