package io.cucumber.java;

import java.lang.reflect.InvocationTargetException;

public interface ExceptionHolder {
    Throwable getPreviousException();
    void clearException();
    void setPreviousException(Throwable e);

    boolean shouldPassThrough(InvocationTargetException e);
}
