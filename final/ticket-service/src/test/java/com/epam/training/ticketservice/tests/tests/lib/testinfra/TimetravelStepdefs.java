package com.epam.training.ticketservice.tests.tests.lib.testinfra;

import com.epam.training.ticketservice.support.db.storedprocedures.H2MockTime;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.internal.SessionImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TimetravelStepdefs {
    private final H2MockTime hmt;
    private final EntityManager em;
    private final PlatformTransactionManager pm;

    @After
    public void clearAfterScenario(){
        hmt.deactivate();
    }
    @Given("we act as if the date is {instant}")
    public void weActAsIfTheDateIs(Instant time) {
        var t = time.atOffset(ZoneOffset.UTC); //TODO figure out time zones
        hmt.activate();
        H2MockTime.setMockTime(t); //TODO make this api database agnostic
    }

    @Then("the date is not {instant}")
    public void theDateIsNot(Instant time) {
        var t = time.atOffset(ZoneOffset.UTC);
        assertThat(queryTime()).isNotEqualTo(t);
    }

    @Then("the date is {instant}")
    public void theDateIs(Instant time) {
        var t = time.atOffset(ZoneOffset.UTC);
        assertThat(queryTime()).isEqualTo(t);
    }

    public OffsetDateTime queryTime(){
        var tt = new TransactionTemplate(pm);
        return tt.execute(this::accept);
    }

    @SneakyThrows
    private OffsetDateTime accept(TransactionStatus transactionStatus) {
        var stmt = em.unwrap(SessionImpl.class)
                .getSession()
                .getJdbcConnectionAccess()
                .obtainConnection()
                .prepareStatement("SELECT CURRENT_TIMESTAMP FROM DUAL");
        try (var rs = stmt.executeQuery()) {
            rs.next();
            return rs.getObject(1, OffsetDateTime.class); //TODO could probably use zoneddatetime? ah but thats probably not db agnostic since its a java thing?
        }
    }
}
