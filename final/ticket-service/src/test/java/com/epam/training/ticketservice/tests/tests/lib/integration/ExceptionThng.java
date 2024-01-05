package com.epam.training.ticketservice.tests.tests.lib.integration;

import com.epam.training.ticketservice.tests.support.ExceptionHolderImpl;
import io.cucumber.java.en.Then;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;

@ScenarioScope
@RequiredArgsConstructor
public class ExceptionThng {
    private final ExceptionHolderImpl eh;
    @Then("I should receive an {exceptionName} with the message {string}")
    public void iShouldReceiveAnWithTheMessage(String exceptionName, String message) {
        ExceptionHolderImpl.assertThrown(eh, exceptionName, message);
    }
}
