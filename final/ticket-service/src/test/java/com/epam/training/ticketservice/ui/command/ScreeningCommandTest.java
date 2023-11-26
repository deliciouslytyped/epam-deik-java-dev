package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("it")
public class ScreeningCommandTest {

    @Autowired
    private Shell shell;

    @SpyBean
    private ScreeningService screeningService;
    @SpyBean
    private MovieService movieService;
    @SpyBean
    private RoomService roomService;
    @SpyBean
    private UserService userService;
    private ScreeningRepository screeningRepository = mock(ScreeningRepository.class);


    @Test
    void testCreateScreeningShouldNotCreateTheScreeningWhenRoomDoesNotExist() {
        //Given
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie It horror 100");

        //When
        Input wrongRoomInput = new Input() {
            @Override
            public String rawText() {
                return "create screening It wrongRoom \"2000-01-01 12:00\"";
            }

            @Override
            public List<String> words() {
                return List.of("create","screening","It","wrongRoom","2000-01-01 12:00");
            }
        };

        //Then
        assertEquals("Room not exists", shell.evaluate(wrongRoomInput));
        verify(screeningRepository, times(0)).save(any());
    }

    @Test
    void testCreateScreeningShouldNotCreateTheScreeningWhenMovieDoesNotExist() {
        //Given
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Its horror 100");

        //When
        Input wrongMovieInput = new Input() {
            @Override
            public String rawText() {
                return "create screening Something szoba \"2000-01-01 12:00\"";
            }

            @Override
            public List<String> words() {
                return List.of("create","screening","Something","szoba","2000-01-01 12:00");
            }
        };

        //Then
        assertEquals("Movie not exists", shell.evaluate(wrongMovieInput));
        verify(screeningRepository, times(0)).save(new Screening("Something","szoba","2000-01-01 12:00"));
    }


}
