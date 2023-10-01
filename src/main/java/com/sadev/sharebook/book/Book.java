package com.sadev.sharebook.book;

import jakarta.validation.constraints.NotBlank;

public class Book {
    @NotBlank
    private String title;
    private Category category;

    private BookStatus bookStatus;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", category=" + category +
                '}';
    }
}
