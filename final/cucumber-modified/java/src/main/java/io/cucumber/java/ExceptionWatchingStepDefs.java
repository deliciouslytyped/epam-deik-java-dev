package io.cucumber.java;

public class ExceptionWatchingStepDefs {
    private Throwable prevExc;

    public Throwable getPreviousException() {
        return prevExc;
    }

    public void clearException(){
        prevExc = null;
    }

    public void setPreviousException(Throwable e) {
        prevExc = e;
    }
}
