package com.epam.training.ticketservice.tests.tests.lib.integration;

import io.cucumber.java.ParameterType;

import java.time.Instant;
import java.time.ZonedDateTime;

public class CucumberConf {

    @ParameterType(".*") //TODO whats the deal with quoting
    public String exceptionName(String exceptionName){ //TODO could do exception type but string is easier given the ambiguity
        return exceptionName;
    }

    @ParameterType("-.*-")
    public Instant instant(String zonedtime){
        return ZonedDateTime.parse(zonedtime.substring(1,zonedtime.length()-1)).toInstant();
        //return Instant.parse(zonedtime);
    }
}
