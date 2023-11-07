package com.epam.training.ticketservice.core.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    private int BASE_PRICE = 1500;

    @Override
    public void createBook(String movieName, String roomName, String date, List<Integer> seats) {
        Book book = new Book(movieName,roomName,date,seats);
        book.setBASE_PRICE(BASE_PRICE);
        bookRepository.save(book);
    }

    @Override
    public void updateBasePrice(int newBasePrice) {
        this.BASE_PRICE = newBasePrice;
    }

    @Override
    public int getBasePrice() {
        return BASE_PRICE;    }
}
