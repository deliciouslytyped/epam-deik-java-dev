package com.epam.training.ticketservice.core.book;

import com.epam.training.ticketservice.core.user.User;
import com.epam.training.ticketservice.core.user.UserDto;
import com.epam.training.ticketservice.core.user.UserRepository;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private int BASE_PRICE = 1500;

    @Override
    public void createBook(String movieName, String roomName, String date, List<String> seats) {
        UserDto userDto = userService.describe().get();
        User user = userRepository.findByUsername(userDto.username()).get();
        Book book = new Book(user,movieName,roomName,date,seats);
        book.setBASE_PRICE(BASE_PRICE);
        bookRepository.save(book);
    }

    @Override
    public void updateBasePrice(int newBasePrice) {
        this.BASE_PRICE = newBasePrice;
    }

    @Override
    public int getBasePrice() {
        return BASE_PRICE;
    }
}
