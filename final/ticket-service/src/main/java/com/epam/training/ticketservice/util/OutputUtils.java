package com.epam.training.ticketservice.util;

import java.util.List;

public interface OutputUtils {
    static <T> String toString(List<T> list, String empty) {
        var sb = new StringBuilder();
        if (list.isEmpty()) {
            sb.append(empty);
        } else {
            list.forEach(r -> sb.append(r).append("\n"));
            sb.setLength(sb.length() - "\n".length());
        }
        return sb.toString();
    }
}
