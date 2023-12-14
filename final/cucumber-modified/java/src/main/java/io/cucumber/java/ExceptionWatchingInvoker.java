package io.cucumber.java;

import io.cucumber.core.backend.CucumberBackendException;
import io.cucumber.core.backend.CucumberInvocationTargetException;
import io.cucumber.core.backend.Located;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class ExceptionWatchingInvoker {

    private ExceptionWatchingInvoker() {

    }

    static Object invoke(Annotation annotation, Method expressionMethod) {
        return invoke(null, annotation, expressionMethod);
    }

    static Object invoke(Located located, Object target, Method method, Object... args) {
        Method targetMethod = targetMethod(target, method);
        return doInvoke(located, target, targetMethod, args);
    }

    private static Method targetMethod(Object target, Method method) {
        Class<?> targetClass = target.getClass();
        Class<?> declaringClass = method.getDeclaringClass();

        // Immediately return the provided method if the class loaders are the
        // same.
        if (targetClass.getClassLoader().equals(declaringClass.getClassLoader())) {
            return method;
        }

        try {
            // Check if the method is publicly accessible. Note that methods
            // from interfaces are always public.
            if (Modifier.isPublic(method.getModifiers())) {
                return targetClass.getMethod(method.getName(), method.getParameterTypes());
            }

            // Loop through all the super classes until the declared method is
            // found.
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                try {
                    return currentClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }

            // The method does not exist in the class hierarchy.
            throw new NoSuchMethodException(String.valueOf(method));
        } catch (NoSuchMethodException e) {
            throw new CucumberBackendException("Could not find target method", e);
        }
    }

    private static Object doInvoke(Located located, Object target, Method targetMethod, Object[] args) {
        boolean accessible = targetMethod.isAccessible();
        try {
            targetMethod.setAccessible(true);
            try {
                var result = targetMethod.invoke(target, args);
                //NOTE the placement of this is a bit interesting; it's suposed to fire after the stepdef following the exception causing stepdef; i.e. it should fire after the stepdef that checks and clears the exception
                // This clause does not run when the exception is thrown, and then is run on the next step if the exception isnt cleared
                if (target instanceof ExceptionWatchingStepDefs t) { // TODO these changes need tests
                    var eh = t.getExceptionHolder();
                    if (eh.getPreviousException() != null) {
                        throw new IllegalStateException("ExceptionWatcher protocol violated, exception needs to be cleared before another exception.", eh.getPreviousException());
                    }
                }
                return result;
            } catch (InvocationTargetException e) {
                if (target instanceof ExceptionWatchingStepDefs t) {
                    if(t.getExceptionHolder().shouldPassThrough(e)){ //TODO not sure this doesnt have issues, but we need to at least pass assertion failures through somehow
                        throw e;
                    }
                    t.getExceptionHolder().setPreviousException(e.getCause());
                    return null; //TODO kinda iffy?
                } else {
                    throw e;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new CucumberBackendException("Failed to invoke " + targetMethod, e);
        } catch (InvocationTargetException e) {
            if (located == null) { // Reflecting into annotations
                throw new CucumberBackendException("Failed to invoke " + targetMethod, e);
            }
            throw new CucumberInvocationTargetException(located, e);
        } finally {
            targetMethod.setAccessible(accessible);
        }
    }

    static Object invokeStatic(Located located, Method method, Object... args) {
        return doInvoke(located, null, method, args);
    }

}
