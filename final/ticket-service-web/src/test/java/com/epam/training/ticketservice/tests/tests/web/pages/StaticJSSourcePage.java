package com.epam.training.ticketservice.tests.tests.web.pages;

import com.epam.training.ticketservice.tests.tests.web.pages.support.CustomFluentPage;
import org.fluentlenium.core.annotation.PageUrl;

@PageUrl("/static/mytable.js")
public class StaticJSSourcePage extends CustomFluentPage {
    // WTF selenium
    // https://stackoverflow.com/questions/6509628/how-to-get-http-response-code-using-selenium-webdriver#comment122220051_6512785
    public StaticJSSourcePage assertLoaded(){
        pageSource().contains("$(document)");
        return this;
    }
}
