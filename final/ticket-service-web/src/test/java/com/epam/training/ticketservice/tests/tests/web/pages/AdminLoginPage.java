package com.epam.training.ticketservice.tests.tests.web.pages;

import com.epam.training.ticketservice.tests.tests.web.pages.support.LoginPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

@PageUrl("/admin")
public class AdminLoginPage extends LoginPage<AdminPage> {
    public AdminPage assertLoggedIn(){
        var ap = newInstance(AdminPage.class);
        ap.assertLoggedIn();
        return ap;
    }
}