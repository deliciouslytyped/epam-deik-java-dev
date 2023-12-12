package com.epam.training.ticketservice.tests.integration;

import io.cucumber.java.ExceptionHolder;
import io.cucumber.java.ExceptionWatchingStepDefs;
import io.cucumber.spring.ScenarioScope;
import lombok.SneakyThrows;
import org.opentest4j.AssertionFailedError;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@ScenarioScope
public class ExceptionHolderImpl implements ExceptionHolder {
    @SneakyThrows
    static void assertThrown(ExceptionHolder o, String exceptionName, @Nullable String message){
        var e = o.getPreviousException();
        assertThat(e).isNotNull();
        var shortname = e.getClass().getSimpleName();
        try {
            assertThat(shortname).isEqualTo(exceptionName); //TODO clean this up
            assertThat(e.getMessage()).isEqualTo(message);
        } catch (AssertionFailedError e2) {
            e2.addSuppressed(e);
            throw e2;
        }
        o.clearException();
    }

    private Throwable prevExc;

    public Throwable getPreviousException() {
        return prevExc;
    }

    public void clearException(){
        prevExc = null;
    }

    public void setPreviousException(Throwable e) {
        prevExc = e;
    }

    @Override
    public boolean shouldPassThrough(InvocationTargetException e) {
        return e.getCause() instanceof AssertionFailedError;
    }
}
