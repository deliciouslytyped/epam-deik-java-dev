package com.epam.training.ticketservice.util;

import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;

/**
 * Rust inspired Results.<br>
 *
 * Contains either the result of an operation, or the occurred error during the operation.
 *
 * @param <T> result type
 * @param <E> error type
 */
public sealed interface Result<T, E extends Exception> permits Result.Ok, Result.Err {
    /**
     * Retrieves the resulting boxed object in case of success.
     * @return The result.
     */
    @UnknownNullability
    T result();

    /**
     * Retrieves the resulting error.
     * @return The error.
     */
    @UnknownNullability
    E error();

    /**
     * @return {@code true} if this {@link Result} indicates a success
     */
    boolean isOk();

    /**
     * @return {@code true} if this {@link Result} indicates an error
     */
    boolean isErr();

    /**
     * @return the State of the result
     */
    State state();

    <N> Result<N, E> mapResult(Function<T, N> mapper);

    <N extends Exception> Result<T, N> mapError(Function<E, N> mapper);

    /**
     * Creates a {@link Result} indicating a success, containing the resulting object.
     *
     * @param result The object
     * @return A successful result.
     * @param <T>
     * @param <E>
     */
    static <T, E extends Exception> Result<T, E> ok(T result) {
        return new Ok<>(result);
    }

    static <T, E extends Exception> Result<T, E> err(E exception) {
        return new Err<>(exception);
    }

    enum State {
        OK, ERROR
    }

    record Ok<T, E extends Exception>(T result) implements Result<T, E> {

        @Override
        public E error() {
            throw new IllegalStateException("Tried to access error value on OK result");
        }

        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public State state() {
            return State.OK;
        }

        @Override
        public <N> Result<N, E> mapResult(Function<T, N> mapper) {
            return new Ok<>(mapper.apply(result));
        }

        @Override
        public <N extends Exception> Result<T, N> mapError(Function<E, N> mapper) {
            throw new IllegalStateException("Tried to map error value on OK result");
        }
    }

    record Err<T, E extends Exception>(E error) implements Result<T, E> {

        @Override
        public T result() {
            throw new IllegalStateException("Tried to access result value on ERR result");
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public State state() {
            return State.ERROR;
        }

        @Override
        public <N> Result<N, E> mapResult(Function<T, N> mapper) {
            throw new IllegalStateException("Tried to map result value on ERR result");
        }

        @Override
        public <N extends Exception> Result<T, N> mapError(Function<E, N> mapper) {
            return new Err<>(mapper.apply(error));
        }
    }
}
