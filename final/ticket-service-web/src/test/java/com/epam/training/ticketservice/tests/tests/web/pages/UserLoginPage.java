package com.epam.training.ticketservice.tests.tests.web.pages;

import com.epam.training.ticketservice.tests.tests.web.pages.support.LoginPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

@PageUrl("/user")
public class UserLoginPage extends LoginPage<UserPage> {

    @Override
    public UserPage assertLoggedIn() {
        var up = newInstance(UserPage.class);
        up.assertLoggedIn();
        return up;
    }
}