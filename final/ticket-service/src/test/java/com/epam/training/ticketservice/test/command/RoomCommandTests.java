package com.epam.training.ticketservice.test.command;

import com.epam.training.ticketservice.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@DirtiesContext
@ActiveProfiles("ci")
public class RoomCommandTests {
    @Autowired
    private Shell shell;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testCreateRoom() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "create room Pedersoli 20 10");

        assertThat(roomRepository.findByName("Pedersoli")).isPresent();
    }

    @Test
    void testUpdateRoom() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "update room Pedersoli 30 15");
        shell.evaluate(() -> "update room nemletezik 30 15");

        var room = roomRepository.findByName("Pedersoli").get();
        assertThat(room.getRows()).isEqualTo(30);
        assertThat(room.getColumns()).isEqualTo(15);
    }

    @Test
    void testDeleteRoom() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "delete room Pedersoli");
        shell.evaluate(() -> "delete room nemletezik");

        assertThat(roomRepository.findByName("Pedersoli")).isEmpty();
    }

    @Test
    void testListRooms() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "create room Girotti 10 10");
        shell.evaluate(() -> "list rooms");

        assertThat(roomRepository.findAll()).hasSize(2);
    }
}
