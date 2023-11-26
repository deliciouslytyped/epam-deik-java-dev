package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("it")
public class RoomCommandTest {

    @Autowired
    private Shell shell;

    @SpyBean
    private RoomService roomService;
    @SpyBean
    private UserService userService;



    @Test
    void testCreateRoomShouldCreateARoomIfTheInputRoomIsValid() {
        shell.evaluate(() -> "sign in privileged admin admin");
        assertEquals("Test room has been created!",
                shell.evaluate(() -> "create room Test 12 10"));
    }

    @Test
    void testDeleteRoomShouldDeleteTheRoomIfRoomExists() {

        shell.evaluate(() -> "sign in privileged admin admin");
        assertEquals("Test room has been deleted!",
                shell.evaluate(() -> "delete room Test"));
        ;
    }


    @Test
    void testListRoomShouldReturnAllRoomsThatOurDatabaseHas() {
        assertEquals("Room szoba with 100 seats, 10 rows and 10 columns",
                shell.evaluate(() -> "list rooms"));
    }

}
