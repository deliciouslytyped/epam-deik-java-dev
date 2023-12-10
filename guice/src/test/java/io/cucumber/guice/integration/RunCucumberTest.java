package io.cucumber.guice.integration;

import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * The Cucumber integration tests use a mixture of annotation and module binding
 * to demonstrate both techniques. The step definition classes are all bound in
 * scenario scope using the @ScenarioScoped annotation. The test object classes
 * are bound using {@link YourModule}.
 */
@RunWith(Cucumber.class)
public class RunCucumberTest {

}
