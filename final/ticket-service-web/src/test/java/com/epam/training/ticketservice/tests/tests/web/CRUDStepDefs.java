package com.epam.training.ticketservice.tests.tests.web;

import com.epam.training.ticketservice.lib.movie.model.MovieMapperImpl;
import com.epam.training.ticketservice.lib.room.model.RoomMapperImpl;
import com.epam.training.ticketservice.tests.tests.web.pages.AdminPage;
import com.epam.training.ticketservice.tests.tests.web.pages.HomePage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.fluentlenium.core.annotation.Page;

@RequiredArgsConstructor
public class CRUDStepDefs extends SeleniumBaseTest {
    @Page
    protected HomePage homepage;
    @Page
    protected AdminPage adminp;
    @Before
    public void beforeScenario(Scenario scenario) {
        this.before(scenario);
        getDriver().manage().deleteAllCookies(); // TODO shouldnt need to do this myself, Im doing something wrong. This doesnt even work.
    }

    @After
    public void afterScenario(Scenario scenario) {
        this.after(scenario);
    }

    @Then("admin can add movie row")
    public void adminCanAddMovieRow() {
        var vals = new String[]{"asdf", "asdf", "1"};
        goTo(adminp)
            .navMovie()
            .addMovie((new MovieMapperImpl()).dtoFromStrings(vals))
            .assertLastRow(vals);
    }

    @Then("admin can add room row")
    public void adminCanAddRoomRow() {
        var vals = new String[]{"asdf", "2", "1"};
        goTo(adminp)
            .navRoom()
            .addRoom((new RoomMapperImpl()).dtoFromStrings(vals))
            .assertLastRow(vals);
    }

    @Then("admin can delete movie row")
    public void adminCanDeleteMovieRow() {
        var lastRow = goTo(adminp)
            .navMovie()
            .getLastRowValues();
        goTo(adminp)
            .navMovie()
            .deleteLast()
            .assertLastRowNot(lastRow);
    }

    @Then("admin can delete room row")
    public void adminCanDeleteRoomRow() {
        var lastRow = goTo(adminp)
            .navRoom()
            .getLastRowValues();
        goTo(adminp)
            .navRoom()
            .deleteLast()
            .assertLastRowNot(lastRow);
    }

    @Then("admin can update movie row")
    public void adminCanUpdateMovieRow() {
        var p = goTo(adminp);
        p.navMovie();
        var newVals = new String[]{"beep", "boop", "100"};
        //TODO, assumes first column is the sole key, which needs to not change
        var vals = p.getLastRowValues();
        newVals[0] = vals[0];
        p
            .editLastRow(newVals)
            .assertLastRow(newVals);
    }

    @Then("admin can update room row")
    public void adminCanUpdateRoomRow() {
        var p = goTo(adminp);
        p.navRoom();
        var newVals = new String[]{"beep", "100", "100"};
        //TODO, assumes first column is the sole key, which needs to not change
        var vals = p.getLastRowValues();
        newVals[0] = vals[0];
        p
                .editLastRow(newVals)
                .assertLastRow(newVals);
    }
}
