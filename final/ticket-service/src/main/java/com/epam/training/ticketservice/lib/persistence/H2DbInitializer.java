package com.epam.training.ticketservice.lib.persistence;
//This is largely unnecessary but I wanted to try stored procedures
// TODO https://github.com/spring-projects/spring-framework/issues/31756
//TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on  afterPropertiesSet lifecycle
import com.epam.training.ticketservice.support.db.storedprocedures.H2StoredProcedures;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
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

    @Override
    public void afterPropertiesSet() {
        var tt = new TransactionTemplate(tm); // Need a transaction to be able to execute the query

        tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("DROP ALIAS IF EXISTS nonempty_update_by_key")
                    .executeUpdate();
            em.createNativeQuery("CREATE ALIAS nonempty_update_by_key FOR '"
                    + H2StoredProcedures.class.getCanonicalName() +".nonempty_update_by_key'")
                    .executeUpdate();
        });
    }
}
