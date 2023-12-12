package com.epam.training.ticketservice.tests.integration;

import io.cucumber.java.ExceptionWatchingStepDefs;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;

public class CucumberConf {

    @ParameterType(".*") //TODO whats the deal with quoting
    public String exceptionName(String exceptionName){ //TODO could do exception type but string is easier given the ambiguity
        return exceptionName;
    }
}
