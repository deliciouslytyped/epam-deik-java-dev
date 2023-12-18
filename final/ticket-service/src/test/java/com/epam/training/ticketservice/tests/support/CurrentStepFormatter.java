package com.epam.training.ticketservice.tests.support;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.net.URI;

public class CurrentStepFormatter implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
    }

    private String locToStr(URI uri, Integer line){
        return uri.toString() + ":" + line.toString();
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

