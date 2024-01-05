package com.epam.training.ticketservice.tests.tests.web.pages;

import com.epam.training.ticketservice.tests.tests.web.pages.support.CustomFluentPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

@PageUrl("/")
public class HomePage extends CustomFluentPage {
    @FindBy(linkText = "User")
    protected FluentWebElement navUserEl;

    @FindBy(linkText = "Admin")
    protected FluentWebElement navAdminEl;

    public <T> T doNav(Class<T> target, FluentWebElement element){
        var currentPage = getDriver().getCurrentUrl();
        element.click();
        awaitPageChangeFrom(currentPage);
        return newInstance(target);
    }

    public AdminLoginPage navAdmin(){ //TODO not sure how this works in the grand scheme of things; assumes we end up on the admin login page, but what if not? / if we arent logged in this ends up on the normal admin page...
        return doNav(AdminLoginPage.class, navAdminEl);
    }

    public UserLoginPage navUser(){ //TODO not sure how this works in the grand scheme of things; assumes we end up on the admin login page, but what if not? / if we arent logged in this ends up on the normal admin page...
        return doNav(UserLoginPage.class, navUserEl);
    }

    public HomePage assertTitle() {
        assertThat(window().title()).contains("Title");
        return this;
    }
}