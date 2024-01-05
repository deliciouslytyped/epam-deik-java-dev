package com.epam.training.ticketservice.tests.tests.lib.integration;

import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningMapper;
import com.epam.training.ticketservice.support.db.storedprocedures.H2MockTime;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.tests.support.ExceptionHolderImpl;
import io.cucumber.java.After;
import io.cucumber.java.ExceptionHolder;
import io.cucumber.java.ExceptionWatchingStepDefs;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class ScreeningStepDefs implements ExceptionWatchingStepDefs {
    private final ScreeningCrudService ss;
    private final ExceptionHolderImpl eh;
    private final ScreeningMapper mapper;

    private final H2MockTime hmt;

    private Object lastResult;

    //TODO can I move these to a common class?
    @Override
    public ExceptionHolder getExceptionHolder() {
        return eh;
    }

    @After
    public void clearAfterScenario(){
        eh.clearException(); // TODO shouldnt this be handled by the exceptionholderimpl being @ScenarioScoped?
        lastResult = null;
        hmt.deactivate();
    }

    //NOTE since this is against the service layer, we use Instants internally. ZonedDateTime is for user facing code to then translate into the interal implementation or something?
    // On one hand thats lossy, because we lose the users timezone, on the other hand the frontend should be handling conersions to-from.
    public void assertScreening(String title, String room, Instant time){
        var key = mapper.dtoToAlternateKey(new ScreeningDto(title, room, time));
        assertThat(ss.getByAlternateKey(key).get())
                .usingRecursiveComparison()
                .isEqualTo(new ScreeningDto(title, room, time));
    }

    @When("I attempt to create a screening of {string} in {string} at {instant}")
    public void iAttemptToCreateAScreeningOfInAtLocalstarttime(String movie, String room, Instant time) {
        ss.create(new ScreeningDto(movie, room, time));
    }

    @Then("the screening for {string} in {string} at {instant} exists")
    public void theScreeningForInAtExists(String movie, String room, Instant time) {
        try {
            ss.create(new ScreeningDto(movie, room, time));
        } catch (AlreadyExistsException ignored) {} // Don't need to do anything special
        assertScreening(movie, room, time);
    }


    /*@And("the screening for {string} in {string} is assigned an id")
    public void theScreeningForInIsAssignedAnId(String movie, String room) {

    }*/
}
