package com.epam.training.ticketservice.test.util;

import com.epam.training.ticketservice.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.*;

public class ResultTests {
    private Result<String, FileNotFoundException> okResult;
    private Result<String, FileNotFoundException> errResult;

    @BeforeEach
    void setup() {
        okResult = Result.ok("test");
        errResult = Result.err(new FileNotFoundException("error"));
    }

    @Test
    void testStates() {
        assertThat(okResult.state()).isEqualTo(Result.State.OK);
        assertThat(errResult.state()).isEqualTo(Result.State.ERROR);
    }

    @Test
    void testCheckMethods() {
        assertThat(okResult.isOk()).isTrue();
        assertThat(okResult.isErr()).isFalse();

        assertThat(errResult.isOk()).isFalse();
        assertThat(errResult.isErr()).isTrue();
    }
}
