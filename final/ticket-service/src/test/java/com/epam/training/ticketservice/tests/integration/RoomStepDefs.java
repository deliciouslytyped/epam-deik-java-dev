package com.epam.training.ticketservice.tests.integration;

import com.epam.training.ticketservice.lib.room.RoomService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.util.exceptions.AlreadyExistsException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class RoomStepDefs {
    public final RoomService rs;

    public void assertRoom(String room, int rows, int cols){
        assertThat(rs.get(room).get())
                .usingRecursiveComparison()
                .isEqualTo(new RoomDto(room, rows, cols));
    }

    //TODO this is pointless because it's tautological? I'm using the interface I should be testing?
    @Given("{string} with -{int}- rows and -{int}- columns of seats already exists")
    public void roomWithRowsAndColumnsOfSeatsAlreadyExists(String room, int rows, int cols) {
        try {
            rs.create(room, rows, cols);
        } catch (AlreadyExistsException ignored) {} // Don't need to do anything special
        assertRoom(room, rows, cols);
    }

    @Given("the room {string} does not exist")
    public void theRoomDoesNotExist(String room) {
        assertThat(rs.get(room).isPresent()).isFalse();
    }

    @When("I attempt to create a {string} with -{int}- rows and -{int}- columns of seats")
    public void iAttemptToCreateAWithRowsAndColumnsOfSeats(String room, int rows, int cols) {
        rs.create(room, rows, cols);
    }

    @Then("{string} exists with -{int}- rows and -{int}- columns of seats")
    public void existsWithRowCountRowsAndColCountColumnsOfSeats(String room, int rows, int cols) {
        assertRoom(room, rows, cols);
    }

}
