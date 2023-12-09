package com.epam.training.ticketservice.tests.integration;

import com.epam.training.ticketservice.lib.movie.MovieService;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.room.RoomService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.util.exceptions.AlreadyExistsException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class MovieStepDefs {
    public final MovieService ms;

    public void assertMovie(String movie, String genre, int runtime){
        assertThat(ms.get(movie).get())
                .usingRecursiveComparison()
                .isEqualTo(new MovieDto(movie, genre, runtime));
    }

    //TODO this is pointless because it's tautological? I'm using the interface I should be testing?
    @Given("the {string} movie {string}, lasting -{int}- minutes")
    public void theMovieLastingMinutes(String movie, String genre, int runtime) {
        try {
            ms.create(movie, genre, runtime);
        } catch (AlreadyExistsException ignored) {} // Don't need to do anything special
        assertMovie(movie, genre, runtime);
    }

    @Given("the movie {string} does not exist")
    public void theMovieDoesNotExist(String movie) {
        assertThat(ms.get(movie).isPresent()).isFalse();
    }

    @When("I attempt to create the {string} movie {string}, lasting -{int}- minutes")
    public void iAttemptToCreateTheMovieLastingMinutes(String genre, String movie, int runtime) {
        ms.create(movie, genre, runtime);
    }

    @Then("the {string} movie {string} exists, lasting -{int}- minutes")
    public void theMovieExistsLastingMinutes(String genre, String movie, int runtime){
        assertMovie(movie, genre, runtime);
    }
}
