package com.epam.training.ticketservice.tests.integration;

import com.epam.training.ticketservice.lib.movie.MovieCrudService;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.lib.util.exceptions.AlreadyExistsException;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//TODO/NOTE apparently its bad practice to try to test exceptions with BDD and it doesnt fit the paradigm well? *rolls eyes*
// So we are using mockito reflection make sure the exception happened

@RequiredArgsConstructor
public class MovieStepDefs {
    private final MovieCrudService ms; //TODO is this the correct place for this?
    private Exception lastException;

    @ParameterType(".*")
    public String exceptionName(String exceptionName){ //TODO could do exception type but string is easier given the ambiguity
        return exceptionName;
    }

    public void assertMovie(String movie, String genre, int runtime){
        // Getting exception is fine because it fails it anyway
        //noinspection OptionalGetWithoutIsPresent
        var m = ms.get(movie).get();
        assertThat(m)
                .usingRecursiveComparison()
                .isEqualTo(new MovieDto(movie, genre, runtime));
    }

    //TODO this is pointless because it's tautological? I'm using the interface I should be testing?
    @Given("the {string} movie {string}, lasting -{int}- minutes")
    public void theMovieLastingMinutes(String genre, String movie, int runtime) {
        try {
            ms.create(new MovieDto(movie, genre, runtime));
        } catch (AlreadyExistsException ignored) {} // Don't need to do anything special
        assertMovie(movie, genre, runtime);
    }

    @Given("the movie {string} does not exist")
    public void theMovieDoesNotExist(String movie) {
        assertThat(ms.get(movie).isPresent()).isFalse();
    }

    @When("I attempt to create the {string} movie {string}, lasting -{int}- minutes")
    public void iAttemptToCreateTheMovieLastingMinutes(String genre, String movie, int runtime) {
        ms.create(new MovieDto(movie, genre, runtime));
    }

    @Then("the {string} movie {string} exists, lasting -{int}- minutes")
    public void theMovieExistsLastingMinutes(String genre, String movie, int runtime){
        assertMovie(movie, genre, runtime);
    }

    @When("I attempt to update the movie {string} to {string} with a runtime of -{int}- minutes")
    public void iAttemptToUpdateTheMovieToWithARuntimeOfRuntimeMinutes(String movie, String newGenre, int newRuntime) {
        ms.update(new MovieDto(movie, newGenre, newRuntime));
    }

    @Then("I should receive an {exceptionName} with the message {string}")
    public void iShouldReceiveAnWithTheMessage(Class<Exception> exceptionT, String message) {
        //hasBeenThrown(exceptionT, message);
    }
}
