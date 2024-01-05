package com.epam.training.ticketservice.tests.tests.web;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.fluentlenium.adapter.SharedMutator;
import org.fluentlenium.adapter.sharedwebdriver.SharedWebDriverContainer;
import org.fluentlenium.configuration.ConfigurationProperties;
import org.fluentlenium.configuration.FluentConfiguration;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@RunWith(Cucumber.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //TODO note documentation note about server and clien transactions rolling back separately
@Profile("ci")
@CucumberContextConfiguration
@CucumberOptions(features = {"classpath:selenium"}, plugin = {"com.epam.training.ticketservice.tests.support.CurrentStepFormatter"})
public class SeleniumTestExecutor {
    @AfterClass
    public static void afterEverything(){//TODO and yet it still doesnt seem to work
        var driver = SharedWebDriverContainer.INSTANCE.getDriver(new SharedMutator.EffectiveParameters<Object>(null, null, ConfigurationProperties.DriverLifecycle.JVM));
        driver.getDriver().close(); // TODO shouldnt need to do this myself, Im doing something wrong.
        driver.getDriver().quit(); // TODO shouldnt need to do this myself, Im doing something wrong.
    }

}
