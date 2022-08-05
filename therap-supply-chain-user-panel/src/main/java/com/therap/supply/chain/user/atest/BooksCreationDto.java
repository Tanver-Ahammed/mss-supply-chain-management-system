package com.therap.supply.chain.user.atest;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BooksCreationDto {

    private List<Book> books = new ArrayList<>();

    // default and parameterized constructor
    public void addBook(Book book) {
        this.books.add(book);
    }

}
