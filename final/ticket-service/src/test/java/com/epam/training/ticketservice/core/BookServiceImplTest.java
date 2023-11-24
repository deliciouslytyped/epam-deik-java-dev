package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.book.Book;
import com.epam.training.ticketservice.core.book.BookRepository;
import com.epam.training.ticketservice.core.book.BookService;
import com.epam.training.ticketservice.core.book.BookServiceImpl;
import com.epam.training.ticketservice.core.user.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private static final Book ENTITY = new Book(new User("root","root", Role.USER),"Something","room1","2004-01-21 12:00", List.of("5,6","6,3"));


    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository);
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final BookServiceImpl underTest = new BookServiceImpl(bookRepository,userService,userRepository);

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
