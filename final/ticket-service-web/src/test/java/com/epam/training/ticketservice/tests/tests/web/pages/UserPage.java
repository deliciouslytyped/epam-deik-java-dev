package com.epam.training.ticketservice.tests.tests.web.pages;

import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

@PageUrl("/user/")
public class UserPage {
    @FindBy(css = "h1")
    protected FluentWebElement header;
    public void assertLoggedIn() {
        assertThat(header.text())
                .isEqualTo("Welcome to the User Page!");
    }
}
