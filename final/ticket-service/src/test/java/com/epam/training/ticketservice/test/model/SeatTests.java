package com.epam.training.ticketservice.test.model;

import com.epam.training.ticketservice.model.Seat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SeatTests {

    @Test
    void testParseEmptyString() {
        var source = "";
        assertThat(Seat.fromString(source)).isEmpty();
    }

    @Test
    void testParseInvalidString() {
        var source = "12,a2";
        assertThatThrownBy(() -> Seat.fromString(source)).isInstanceOf(NumberFormatException.class);
    }

    @Test
    void testParseSingleSeat() {
        var source = "12,32";
        var result = Seat.fromString(source);
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(new Seat(12, 32));
    }

    @Test
    void testBoundCheck() {
        var seat = new Seat(1, 1);
        assertThat(seat.inBounds(1, 1)).isTrue();
        assertThat(seat.inBounds(1, 2)).isTrue();
        assertThat(seat.inBounds(2, 1)).isTrue();
        assertThat(seat.inBounds(2, 2)).isTrue();
        assertThat(seat.inBounds(0, 1)).isFalse();
        assertThat(seat.inBounds(1, 0)).isFalse();
        assertThat(seat.inBounds(0, 0)).isFalse();
        assertThat(seat.inBounds(-1, 1)).isFalse();
        assertThat(seat.inBounds(1, -1)).isFalse();
        assertThat(seat.inBounds(-1, -1)).isFalse();
    }
}
