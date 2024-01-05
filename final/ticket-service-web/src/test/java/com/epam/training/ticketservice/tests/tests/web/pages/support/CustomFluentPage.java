package com.epam.training.ticketservice.tests.tests.web.pages.support;

import org.fluentlenium.core.FluentPage;

import java.util.concurrent.TimeUnit;

public class CustomFluentPage extends FluentPage {
    public void awaitPageChangeFrom(String currentUrl){
        await()
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> !getDriver().getCurrentUrl().equals(currentUrl));
    }
}
