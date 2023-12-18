package com.epam.training.ticketservice.tests.integration;

import com.epam.training.ticketservice.lib.movie.MovieCrudService;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
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

//TODO/NOTE apparently its bad practice to try to test exceptions with BDD and it doesnt fit the paradigm well? *rolls eyes*
// So we are using mockito reflection make sure the exception happened

@RequiredArgsConstructor
//TODO composition over inheritance
public class MovieStepDefs implements ExceptionWatchingStepDefs {
    private final MovieCrudService ms; //TODO is this the correct place for this?
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

    public void assertMovie(String movie, String genre, int runtime){
        // Getting exception is fine because it fails it anyway
        //noinspection OptionalGetWithoutIsPresent
        var m = ms.get(movie).get();
        System.out.println("got value");
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

    @When("I attempt to delete the movie {string}")
    public void iAttemptToDeleteTheMovie(String movie) {
        ms.delete(movie);
    }

    @When("I request a list of all movies")
    public void iRequestAListOfAllMovies() {
        lastResult = ms.list();
    }

    @Then("I should receive a list of all the movies with the titles {string}")
    public void iShouldReceiveAListOfAllTheMoviesWithTheTitles(String titles) {
        assertThat(((List<MovieDto>)lastResult).stream().map(MovieDto::getTitle))
                .containsExactlyInAnyOrder(titles.split(", ?"));
        lastResult = null; //TODO makethis protocol?
    }

    @Then("I should receive an empty movie list")
    public void iShouldReceiveAnEmptyList() {
        assertThat((List<MovieDto>)lastResult).isEmpty();
        lastResult = null; //TODO makethis protocol?
    }

    //This is kinda funky / redundant with the above?
    @Given("there are no movies in the system")
    public void thereAreNoMoviesInTheSystem() {
        assertThat(ms.list()).isEmpty();
    }
}
