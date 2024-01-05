package com.training.epam.ticketservice.at;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class GenericCliProcessStepDefs {

    private static final int OUTPUT_TIMEOUT = 30000;
    private static final String jarFile = "../ticket-service-cli/target/ticket-service-cli-0.0.1-SNAPSHOT.jar";

    private ProcessUnderTest cliProcess;

    public GenericCliProcessStepDefs(ProcessUnderTest cliProcess) {
        this.cliProcess = cliProcess;
    }

    @Given("the application is started")
    public void applicationStarted() throws IOException, InterruptedException {
        if (!(new File(jarFile)).isFile()){
            throw new FileNotFoundException("The path " + jarFile + " does not exist. It should be your built JAR file.");
        }

        //TODO for debugging: -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:8000
        var command = "java -jar -Dspring.profiles.active=ci " + jarFile;
        System.out.println("Starting command:" + command);
        cliProcess.run(command);
    }

    @Given("the prompt containing {string} is printed")
    public void promptReturned(String expectedPrompt) throws InterruptedException, ExecutionException, TimeoutException {
        cliProcess.waitForOutput(expectedPrompt, OUTPUT_TIMEOUT);
    }

    @When("the user types the {string} command")
    public void theUserTypes(String command) throws IOException {
        cliProcess.writeOnInput(command + System.lineSeparator());
    }

    @Then("the next line of the output is {string}")
    public void theNextLineOfOutputContains(String expectedOutput) throws InterruptedException, ExecutionException, TimeoutException {
        String actualLine = cliProcess.readNextLine(OUTPUT_TIMEOUT);
        assertThat(actualLine, equalTo(expectedOutput));
    }

    @After
    public void cleanup() {
        cliProcess.close();
    }
}
