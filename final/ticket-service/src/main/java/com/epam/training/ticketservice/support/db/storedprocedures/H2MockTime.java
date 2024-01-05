package com.epam.training.ticketservice.support.db.storedprocedures;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.h2.engine.SessionLocal;
import org.h2.expression.function.CurrentDateTimeValueFunction;
import org.h2.jdbc.JdbcConnection;
import org.h2.value.ValueTimestampTimeZone;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.time.OffsetDateTime;

//Oh wow this was actually made for this https://github.com/h2database/h2database/issues/2553

@Component
@RequiredArgsConstructor
public class H2MockTime {
    // https://stackoverflow.com/questions/65719302/testing-with-mocked-value-for-current-timestamp-on-h2
    public static OffsetDateTime mockTime;
    public static boolean active = false;

    public static void setMockTime(OffsetDateTime mt){
        mockTime = mt;
    }

    public void activate() { //TODO check alias
        active = true;
    }

    public void deactivate() {
        // We cant do it like this because changing aluases causes the active transaction to commit
        /*tt.executeWithoutResult(transactionStatus -> {
            em.createNativeQuery("DROP ALIAS IF EXISTS \"CURRENT_TIMESTAMP\"")
                    .executeUpdate();
        });*/
        active = false;
    }

    public static OffsetDateTime getMockTime(Connection conn) {
        var c = (JdbcConnection) conn;
        var s = (SessionLocal) c.getSession();
        if(active) {
            return mockTime;
        } else {
            var v = (ValueTimestampTimeZone) (new CurrentDateTimeValueFunction(CurrentDateTimeValueFunction.CURRENT_TIMESTAMP, -1))
                    .getValue(s);
            return OffsetDateTime.parse(v.getISOString());// TODO since we're using h2 internal stuff anyway, could look into returning a homogeneous h2 object instead of converting to offsetdatetime
        }
    }
}
