package com.example.sqllite.book;

public class Book {
    private int resource_id;
    private String title;

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Book(int resource_id, String title) {
        this.resource_id = resource_id;
        this.title = title;
    }
}
