package com.epam.training.ticketservice.tests.tests.web;

import com.epam.training.ticketservice.tests.tests.web.pages.HomePage;
import com.epam.training.ticketservice.tests.tests.web.pages.StaticJSSourcePage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.fluentlenium.core.annotation.Page;

import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;

@RequiredArgsConstructor
public class LoginStepDefs extends SeleniumBaseTest {
    //@Page
    //protected FluentPage page; //TODO does this work? making the type more specific than the parent.
    @Page
    protected HomePage homepage;

    @Page
    protected StaticJSSourcePage staticp;

    // TODO I dont know if I actually need these. Docs say you shouldnt put them in the base class.

    @When("Enter username {string} and password {string}")
    public void enterCredentials(String uname, String pw){

    }

    @Then("Login succeeded")
    public void loginIsSuccessful(){

    }

    @Before
    public void beforeScenario(Scenario scenario) {
        this.before(scenario);
        getDriver().manage().deleteAllCookies(); // TODO shouldnt need to do this myself, Im doing something wrong. This doesnt even work.
    }

    @After
    public void afterScenario(Scenario scenario) {
        this.after(scenario);
    }
    @Then("the home page is accessible")
    public void theHomePageIsAccessible() {
        goTo(homepage)
                .assertTitle();
    }

    @Then("the admin user can log in with the admin user credentials")
    public void theAdminUserCanLogInWithTheAdminUserCredentials() {
        getDriver().manage().deleteAllCookies();
        goTo(homepage)
                .navAdmin()
                .typeUsernameIn("admin")
                .typePasswordIn("adm")
                .submit()
                .assertLoggedIn();
    }

    @Then("the admin user can not log in with the normal user credentials")
    public void theAdminUserCanNotLogInWithTheNormalUserCredentials() {
        getDriver().manage().deleteAllCookies();
        goTo(homepage)
                .navAdmin()
                .typeUsernameIn("steve")
                .typePasswordIn("pls")
                .submit()
                .assertNotLoggedIn();
    }

    @Then("the user can log in with the normal user credentials")
    public void theUserCanLogInWithTheNormalUserCredentials(){
        getDriver().manage().deleteAllCookies();
        goTo(homepage)
                .navUser()
                .typeUsernameIn("steve")
                .typePasswordIn("pls")
                .submit()
                .assertLoggedIn();
    }


    @Then("the user can not log in with the admin user credentials")
    public void theUserCanNotLogInWithTheAdminUserCredentials(){
        getDriver().manage().deleteAllCookies();
        goTo(homepage)
                .navUser()
                .typeUsernameIn("admin")
                .typePasswordIn("adm")
                .submit()
                .assertNotLoggedIn();
    }

    @Then("static resources are accessible")
    public void staticResourcesAreAccessible() {
        goTo(staticp)
                .assertLoaded();
    }
}
