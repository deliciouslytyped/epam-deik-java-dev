package com.epam.training.ticketservice.tests.integration;

import io.cucumber.java.ParameterType;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@RunWith(Cucumber.class)
@SpringBootTest
@Profile("ci")
@CucumberContextConfiguration
@CucumberOptions(features = {"classpath:serviceimpl",}, plugin = {"com.epam.training.ticketservice.tests.support.CurrentStepFormatter"})
public class TestExecutor {
}
