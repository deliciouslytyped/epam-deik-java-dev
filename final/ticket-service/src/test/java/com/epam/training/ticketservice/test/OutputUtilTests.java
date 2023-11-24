package com.epam.training.ticketservice.test;

import com.epam.training.ticketservice.util.OutputUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class OutputUtilTests {

    @Test
    void testEmpty() {
        var message = "No elements!";
        var result = OutputUtils.toString(List.of(), message);

        assertThat(result).isEqualTo(message);
    }

    @Test
    void testListOfStrings() {
        var elements = List.of("first", "second", "third");
        var result = OutputUtils.toString(elements, "");

        var expected = """
                first
                second
                third""";
        assertThat(result).isEqualTo(expected);
    }
}
