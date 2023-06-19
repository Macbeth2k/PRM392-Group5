package com.example.sqllite.category;

import com.example.sqllite.book.Book;

import java.util.List;

public class Category {
    private String name;
    private List<Book> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Category(String name, List<Book> books) {
        this.name = name;
        this.books = books;
    }
}
