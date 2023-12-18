package com.epam.training.ticketservice.tests.integration;

import com.epam.training.ticketservice.lib.room.RoomCrudService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.tests.support.ExceptionHolderImpl;
import io.cucumber.java.After;
import io.cucumber.java.ExceptionHolder;
import io.cucumber.java.ExceptionWatchingStepDefs;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class RoomStepDefs implements ExceptionWatchingStepDefs {
    private final RoomCrudService rs;
    private final ExceptionHolderImpl eh;

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
    }

    public void assertRoom(String room, int rows, int cols){
        assertThat(rs.get(room).get())
                .usingRecursiveComparison()
                .isEqualTo(new RoomDto(room, rows, cols));
    }

    //TODO this is pointless because it's tautological? I'm using the interface I should be testing?
    @Given("{string} with -{int}- rows and -{int}- columns of seats already exists")
    public void roomWithRowsAndColumnsOfSeatsAlreadyExists(String room, int rows, int cols) {
        try {
            rs.create(new RoomDto(room, rows, cols));
        } catch (AlreadyExistsException ignored) {} // Don't need to do anything special
        assertRoom(room, rows, cols);
    }

    @Given("the room {string} does not exist")
    public void theRoomDoesNotExist(String room) {
        assertThat(rs.get(room).isPresent()).isFalse();
    }

    @When("I attempt to create a {string} with -{int}- rows and -{int}- columns of seats")
    public void iAttemptToCreateAWithRowsAndColumnsOfSeats(String room, int rows, int cols) {
        rs.create(new RoomDto(room, rows, cols));
    }

    @Then("{string} exists with -{int}- rows and -{int}- columns of seats")
    public void existsWithRowCountRowsAndColCountColumnsOfSeats(String room, int rows, int cols) {
        assertRoom(room, rows, cols);
    }

    @When("I attempt to update the room {string} to have -{int}- rows and -{int}- columns of seats")
    public void iAttemptToUpdateTheRoomToHaveNewRowCountRowsAndNewColCountColumnsOfSeats(String room, int rows, int cols) {
        rs.update(new RoomDto(room, rows, cols));
    }

    @When("I attempt to delete the room {string}")
    public void iAttemptToDeleteTheRoom(String room) {
        rs.delete(room);
    }

    @When("I request a list of all rooms")
    public void iRequestAListOfAllRooms() {
        lastResult = rs.list();
    }

    @Then("I should receive a list of all the rooms with the names {string}")
    public void iShouldReceiveAListOfAllTheRoomsWithTheNames(String names) {
        assertThat(((List<RoomDto>)lastResult).stream().map(RoomDto::getName))
                .containsExactlyInAnyOrder(names.split(", ?"));
        lastResult = null; //TODO makethis protocol?
    }

    @Then("I should receive an empty room list")
    public void iShouldReceiveAnEmptyList() {
        assertThat((List<RoomDto>)lastResult).isEmpty();
        lastResult = null; //TODO makethis protocol?
    }

    //This is kinda funky / redundant with the above?
    @Given("there are no rooms in the system")
    public void thereAreNoRoomsInTheSystem() {
        assertThat(rs.list()).isEmpty();
    }}
