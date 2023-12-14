package com.epam.training.ticketservice.lib.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

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
