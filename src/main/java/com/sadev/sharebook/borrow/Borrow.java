package com.sadev.sharebook.borrow;

import com.sadev.sharebook.book.Book;
import com.sadev.sharebook.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User borrower;
    //preteur
    @ManyToOne
    private User lender;
    @ManyToOne
    private Book book;
    private LocalDate askDate;
    private LocalDate closeDate;

    public Borrow() {
    }
    public Borrow(User borrower, User lender, Book book) {
        this.borrower = borrower;
        this.lender = lender;
        this.book = book;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public User getLender() {
        return lender;
    }

    public void setLender(User lender) {
        this.lender = lender;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
