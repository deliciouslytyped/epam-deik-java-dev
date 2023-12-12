package com.epam.training.ticketservice.tests.integration;

import io.cucumber.java.en.Then;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@ScenarioScope
@RequiredArgsConstructor
public class ExceptionThng {
    private final ExceptionHolderImpl eh;
    @Then("I should receive an {exceptionName} with the message {string}")
    public void iShouldReceiveAnWithTheMessage(String exceptionName, String message) {
        ExceptionHolderImpl.assertThrown(eh, exceptionName, message);
    }
}
