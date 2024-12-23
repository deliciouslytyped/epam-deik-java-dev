package com.training.epam.ticketservice.at;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
//TODO shouldnt need to specify the glue path explicitly
@CucumberOptions(features = "classpath:features", glue="com.training.epam.ticketservice.at")
public class TestExecutor {

}
