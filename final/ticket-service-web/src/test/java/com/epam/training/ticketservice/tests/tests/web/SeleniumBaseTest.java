package com.epam.training.ticketservice.tests.tests.web;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.fluentlenium.adapter.cucumber.FluentCucumberTest;
import org.fluentlenium.configuration.ConfigurationProperties;
import org.fluentlenium.configuration.FluentConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//TODO I dont quite understand whats going on because fluent interacts with both cucumber and spring?
@FluentConfiguration(driverLifecycle = ConfigurationProperties.DriverLifecycle.JVM) //TODO does this work //TODO "cucumber doesnt support the CLASS lifetime....
public class SeleniumBaseTest extends FluentCucumberTest { //TODO what does fluentcucumbertest actually do
    @Autowired
    protected ChromeOptions options;

    @LocalServerPort
    protected Integer port;

    //TODO dunno if these are recommended, https://notes.dmitriydubson.com/testing/e2e-testing/fluentlenium/ uses it
    @Override
    public String getBaseUrl(){
        return "http://localhost:" + port;
    }
    @Override
    public WebDriver newWebDriver() { //TODO how to get it to stay open if I want
        ChromeDriver driver = new ChromeDriver(options);
        return driver;
    }
}
