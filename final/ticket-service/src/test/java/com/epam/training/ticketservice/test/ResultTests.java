package com.epam.training.ticketservice.test;

import com.epam.training.ticketservice.util.Result;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.*;

public class ResultTests {

    @Test
    void testStates() {
        Result<String, FileNotFoundException> okResult = Result.ok("test");
        Result<String, FileNotFoundException> errResult = Result.err(new FileNotFoundException("error"));
        assertThat(okResult.state()).isEqualTo(Result.State.OK);
        assertThat(errResult.state()).isEqualTo(Result.State.ERROR);
    }
}
