package com.epam.training.ticketservice.tests.tests.web.pages.support;

import com.epam.training.ticketservice.tests.tests.web.pages.AdminPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class LoginPage<Z> extends CustomFluentPage {
    @FindBy(id = "username")
    protected FluentWebElement username;
    @FindBy(id = "password")
    protected FluentWebElement password;
    @FindBy(xpath = "//button[contains(.,\'Sign in\')]")
    protected FluentWebElement submitEl;

    @FindBy(css = "h2[class='form-signin-heading']")
    protected FluentWebElement signin;

    public LoginPage<Z> typeUsernameIn(String un){
        username.write(un);
        return this;
    }

    public LoginPage<Z> typePasswordIn(String pw){
        password.write(pw);
        return this;
    }

    public LoginPage<Z> submit(){
        var currenturl = getDriver().getCurrentUrl();
        submitEl.submit();
        awaitPageChangeFrom(currenturl); // TODO what if url not changed...? for example on login fail? (actually stuff after ? added, so...?
        return this;
    }

    public abstract Z assertLoggedIn(); //TODO these return types are a little funky
    public void assertNotLoggedIn() { //TODO these should actually go on the login page class but then we have dependent typing issues? need to return two different class types
        assertThat(signin).isNotNull();
    }
}