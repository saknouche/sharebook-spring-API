package com.sadev.sharebook.schema;

import com.sadev.sharebook.book.BookStatus;
import com.sadev.sharebook.book.Category;

public class BookResponse {
    private Integer id;
    private String title;
    private Category category;
    private BookStatus status;
    private UserInfoResponse lender;
    public BookResponse() {
    }

    public BookResponse(Integer id, String title, Category category, BookStatus status) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.status = status;
    }

    public BookResponse(Integer id, String title, Category category, BookStatus status, UserInfoResponse lender) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.status = status;
        this.lender = lender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public UserInfoResponse getLender() {
        return lender;
    }

    public void setLender(UserInfoResponse lender) {
        this.lender = lender;
    }
}
