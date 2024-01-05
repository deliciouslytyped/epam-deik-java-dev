package com.training.epam.ticketservice.at;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessUnderTest implements AutoCloseable {

    private static final int JVM_STARTUP_FAILURE_WAIT_TIME = 150;
    private static final int DELAY_BEFORE_CLEANING_PROCESS_OUTPUT = 1000;

    private Process process;
    private BufferedReader output;
    private Writer input;

    public void run(String command) throws IOException, InterruptedException {
        if (this.process != null && process.isAlive()) {
            return;
        }
        process = Runtime.getRuntime().exec(command);
        System.out.println("PID is:" + process.pid());
        output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        input = new OutputStreamWriter(process.getOutputStream());
        Thread.sleep(JVM_STARTUP_FAILURE_WAIT_TIME);
        verifyProcessIsRunning();
    }

    public void waitForOutput(String expectedOutput, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        verifyProcessIsRunning();
        runWithTimeout(() -> readOutputUntil(expectedOutput), timeout);
    }

    public String readNextLine(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        verifyProcessIsRunning();
        return runWithTimeout(() -> output.readLine(), timeout);
    }

    @Override
    public void close() {
        process.destroy();
    }

    public void writeOnInput(String command) throws IOException {
        verifyProcessIsRunning();
        clearOutput(DELAY_BEFORE_CLEANING_PROCESS_OUTPUT);
        var theInput = command + System.lineSeparator();
        System.out.println(theInput);
        input.write(theInput);
        input.flush();
    }

    private void clearOutput(long delayBeforeCleaning) throws IOException {
        try {
            Thread.sleep(delayBeforeCleaning);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (output.ready()) {
            int c = output.read();
            System.out.print((char)c);
        }
    }

    private Void readOutputUntil(String expectedOutput) throws IOException {
        String actualString = "";
        do {
            int c = output.read();
            System.out.print((char)c);
            if (c == -1) {
                throw new IOException("Reached EOF before receiving '" + expectedOutput + "'");
            }
            actualString += (char) c;
            if (actualString.length() > expectedOutput.length()) {
                actualString = actualString.substring(1);
            }
        } while (!actualString.equals(expectedOutput));
        return null; // Void for the sake of generics
    }

    private <T> T runWithTimeout(Callable<T> callable, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        SimpleTimeLimiter timeLimiter = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());
        return timeLimiter.callWithTimeout(callable, timeout, TimeUnit.MILLISECONDS);
    }

    private void verifyProcessIsRunning() {
        if (this.process == null || !process.isAlive()) {
            throw new AssertionError("The service is not running.");
        }
    }
}
