package com.epam.training.ticketservice.test;

import org.springframework.shell.Input;

import java.util.Arrays;
import java.util.List;

public interface In extends Input {

    @Override
    default List<String> words() {
        var words = rawText().split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        return Arrays.stream(words).map(s -> {
            if (s.startsWith("\"") && s.endsWith("\"")) {
                return s.substring(1, s.length() - 1);
            }
            return s;
        }).toList();
    }
}
