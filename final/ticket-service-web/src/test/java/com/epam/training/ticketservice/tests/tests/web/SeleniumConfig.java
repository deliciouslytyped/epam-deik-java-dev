package com.epam.training.ticketservice.tests.tests.web;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PreDestroy;

//TODO look at the fluentlenium spring and cucumber integration libraries

@Configuration
@PropertySource(value = "classpath:selenium.properties")
public class SeleniumConfig {
    @Value("${selenium.webdriver.chrome.driver}")
    protected String chromeDriverPath;

    @Value("${selenium.chromeBinary}")
    protected String chromeBinaryPath;

    @Bean
    public ChromeOptions chromeDriverOptions() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.setBinary(chromeBinaryPath);
        options.addArguments("--remote-allow-origins=*"); //TODO this is bad but I dont know the localhost condition isnt fitting??
        return options;
    }
}
