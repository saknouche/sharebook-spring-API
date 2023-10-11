package com.sadev.sharebook.schema;

import java.time.LocalDate;

public class BorrowResponse {
    private Integer id;
    private UserInfoResponse borrower;
    private UserInfoResponse lender;
    private String bookTitle;
    private String bookCategory;
    private LocalDate askDate;
    private LocalDate closeDate;

    public BorrowResponse() {
    }

    public BorrowResponse(Integer id, UserInfoResponse borrower, UserInfoResponse lender, String bookTitle, String bookCategory, LocalDate askDate, LocalDate closeDate) {
        this.id = id;
        this.borrower = borrower;
        this.lender = lender;
        this.bookTitle = bookTitle;
        this.bookCategory = bookCategory;
        this.askDate = askDate;
        this.closeDate = closeDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserInfoResponse getBorrower() {
        return borrower;
    }

    public void setBorrower(UserInfoResponse borrower) {
        this.borrower = borrower;
    }

    public UserInfoResponse getLender() {
        return lender;
    }

    public void setLender(UserInfoResponse lender) {
        this.lender = lender;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public LocalDate getAskDate() {
        return askDate;
    }

    public void setAskDate(LocalDate askDate) {
        this.askDate = askDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }
}
