package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.book.persistence.Booking;
import com.epam.training.ticketservice.core.book.persistence.BookingRepository;
import com.epam.training.ticketservice.core.book.persistence.Seat;
import com.epam.training.ticketservice.core.book.service.BookingServiceImpl;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.Role;
import com.epam.training.ticketservice.core.user.persistence.User;
import com.epam.training.ticketservice.core.user.persistence.UserRepository;
import com.epam.training.ticketservice.core.user.service.UserServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private static final Booking ENTITY = null;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final BookingServiceImpl underTest = new BookingServiceImpl(bookingRepository,userRepository,screeningRepository);

    /*
    @Test
    void testCreateBookShouldStoreTheGivenBookWhenTheInputBookIsValid() {
        //Given
        when(bookRepository.save(ENTITY)).thenReturn(ENTITY);
        when(userService.describe()).thenReturn(Optional.of(new UserDto("root",Role.USER)));

        //When
        underTest.createBook("Something","room1","2004-01-21 12:00",List.of("5,6","6,3"));

        //Then
        verify(bookRepository).save(ENTITY);

    }
*/


}
