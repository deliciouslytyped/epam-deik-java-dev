package com.epam.training.ticketservice.lib.db;
//This is unnecessary but I wanted to try stored procedures


import lombok.RequiredArgsConstructor;
import org.h2.api.ErrorCode;
import org.h2.message.DbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.h2.message.DbException.getJdbcSQLException;

@Component
@DependsOn("entityManagerFactory")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix="spring.jpa", name="database-platform", havingValue="org.hibernate.dialect.H2Dialect") //TODO check at least one of these isloaded
public class H2DbInitializer implements InitializingBean {
    private final EntityManager em;
    private final PlatformTransactionManager tm;

    private static final Logger logger = LoggerFactory.getLogger(H2DbInitializer.class);

    //TODO this is better (Actually, may be broken) but i still cant actually prove the upper and lower bounds on this
    @Override
    public void afterPropertiesSet() throws Exception {
        var tt = new TransactionTemplate(tm);

        //Create stored procedure for updates that throw exceptions
        tt.executeWithoutResult(transactionStatus -> {
            // probably ran into this https://github.com/h2database/h2database/issues/2529
            em.createNativeQuery("DROP ALIAS IF EXISTS nonempty_update_by_key").executeUpdate();
            em.createNativeQuery("CREATE ALIAS nonempty_update_by_key FOR '"+ H2DbInitializer.class.getCanonicalName() +".nonempty_update_by_key'").executeUpdate();
        });
    }

    //https://www.h2database.com/html/commands.html#create_alias
    // nonempty_update_by_key(tablename,key,keyval,...value pairs...)
    // https://groups.google.com/g/h2-database/c/rMIlIPNUA9c What you want in your case is to _not_ convert the value. This iscurrently not supported, and I wonder if it can be supported whenusing static methods (and no hacks). Also, the return type is Object.I guess that means the returned data type is unknown until the methodis called? One workaround is to pass an Object array and return anObject array
    // So, since h2 seems to force us into not using Object..., we use the Object[] mentoned i the thread...
    public static int nonempty_update_by_key(Connection conn, String tableName, Object... args) throws SQLException {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        StringBuilder colSpec = new StringBuilder();
        assert args.length % 2 == 0;
        for (int i = 2; i < args.length; i += 2) {
            params.put((String) args[i], args[i + 1]);
            colSpec.append(args[i]);
            colSpec.append(" = ?");
            if (i != args.length - 2) {
                colSpec.append(",");
            }
        }
        params.put((String) args[0], args[1]); // the key goes in the where clause at the end
        String searchSpec = args[0] + " = ?";
        var sql = "UPDATE " + tableName + " SET " + colSpec + " WHERE " + searchSpec;
        logger.debug(sql);
        int affectedRows;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { //TODO needs sanitization
            //Should work though the doc says
            // "Returns: either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing"
            //https://stackoverflow.com/questions/15299882/how-to-count-deleted-rows-in-a-h2-database
            int index = 0;
            // Indexing starts from 1
            for (var par : params.values()) {
                stmt.setObject(index + 1, par);
                logger.debug("Binding " + par + " to index " + (index + 1));
                index++;
            }
            affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                //throw new SQLException("Update affects no rows.", Integer.toString(ErrorCode.CHECK_CONSTRAINT_VIOLATED_1)); //TODO check this actually work
                throw getJdbcSQLException(ErrorCode.CHECK_CONSTRAINT_VIOLATED_1, null, "CHECK_UPDATE_HAS_ROWS: ");
            }
        }
        return affectedRows;
    }
}
