package com.epam.training.ticketservice.tests.support;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class CurrentStepFormatter implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
    }

    @SneakyThrows
    //TODO fix this so it points at the original source, not the test classes directory
    private String locToStr(URI uri, Integer line){
        // because we are now using classpath:
        // this is chatgptd. Is there a less messy way to do this?
        // probably not due to javas ability to load classes from urls etc whatever
        var path = Paths.get(getClass().getClassLoader().getResource(uri.getSchemeSpecificPart()).toURI()).toString();
        return path + ":" + line.toString();
    }

    private Integer getLine(Step v){
        return v.getLocation().getLine();
    }

    private Integer getLine(TestCase v){
        return v.getLocation().getLine();
    }
    private void handleTestCaseStarted(TestCaseStarted event) {
        var tcase = event.getTestCase();
        var uri = tcase.getUri();
        System.out.println("=====================");
        System.out.println("Scenario: " + event.getTestCase().getName() + " - (" + locToStr(uri, getLine(tcase)) + ")");
        for (TestStep s : tcase.getTestSteps()) {
            if (s instanceof PickleStepTestStep step) {
                var stp = step.getStep();
                System.out.println(step.getStep().getKeyword() + ": " + stp.getText() + " - (" + locToStr(uri, getLine(stp)) + ")");
            }
        }
    }
}

