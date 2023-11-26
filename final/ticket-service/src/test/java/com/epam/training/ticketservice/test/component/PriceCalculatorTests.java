package com.epam.training.ticketservice.test.component;

import com.epam.training.ticketservice.component.PriceCalculator;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


public class PriceCalculatorTests {
    private Movie movie;
    private Room room;
    private Screening screening;
    private PriceCalculator calculator;
    private int basePrice;

    @BeforeEach
    void setup() {
        movie = new Movie("Toy Story 3", "animation", 350);
        room = new Room("garage", 2, 3);
        screening = new Screening(movie, room, LocalDateTime.now());
        calculator = new PriceCalculator();
        basePrice = 1500;
    }

    @Test
    void testBaseline() {
        int result = calculator.calculate(screening, basePrice, 1);

        assertThat(result).isEqualTo(basePrice);
    }

    @Test
    void testWithMovieComponent() {
        var component = new PriceComponent("movieComponent", 1000);
        movie.setPriceComponent(component);

        int result = calculator.calculate(screening, basePrice, 1);
        assertThat(result).isEqualTo(basePrice + component.getAmount());
    }

    @Test
    void testWithMRoomComponent() {
        var component = new PriceComponent("roomComponent", 3000);
        movie.setPriceComponent(component);

        int result = calculator.calculate(screening, basePrice, 1);
        assertThat(result).isEqualTo(basePrice + component.getAmount());
    }

    @Test
    void testWithScreeningComponent() {
        var component = new PriceComponent("movieComponent", -500);
        movie.setPriceComponent(component);

        int result = calculator.calculate(screening, basePrice, 1);
        assertThat(result).isEqualTo(basePrice + component.getAmount());
    }
}
