package com.epam.training.ticketservice.tests.tests.web;

import io.cucumber.java.en.Then;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.annotation.Page;

import static org.assertj.core.api.Assertions.assertThat;

public class SeleniumBaseSteps extends SeleniumBaseTest {
    //TODO this uses fluentleniums own inector
    // what happens if there are multiple page objects?
    @Page
    protected FluentPage page; //TODO is this going to be correct with this /\ shape?

    @Then(value = "Title contains {string}")
    public void titleContains(String str) {
        assertThat(page.window().title()).contains(str);
    }
}
