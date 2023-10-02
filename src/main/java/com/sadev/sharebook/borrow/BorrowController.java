package com.sadev.sharebook.borrow;

import com.sadev.sharebook.book.Book;
import com.sadev.sharebook.book.BookController;
import com.sadev.sharebook.book.BookRepository;
import com.sadev.sharebook.book.BookStatus;
import com.sadev.sharebook.user.User;
import com.sadev.sharebook.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class BorrowController {
    @Autowired
    BorrowRepository borrowRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/borrows")
    public ResponseEntity getMyBorrows() {
        List<Borrow> borrows = borrowRepository.findByBorrowerId(BookController.getUserConnectedId());
        if (borrows == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(borrows, HttpStatus.OK);
    }

    @PostMapping("/borrows/{bookId}")
    public ResponseEntity createBorrow(@PathVariable("bookId") String bookId) {
        Integer userConnectedId = BookController.getUserConnectedId();
        Optional<User> borrower = userRepository.findById(userConnectedId);
        Optional<Book> book = bookRepository.findById(Integer.valueOf(bookId));
        if (borrower.isPresent() && book.isPresent() && book.get().getBookStatus().equals(BookStatus.FREE)) {
            Borrow borrow = new Borrow();
            borrow.setBorrower(borrower.get());
            Book bookEntity = book.get();
            borrow.setLender(bookEntity.getUser());
            borrow.setBook(bookEntity);
            borrow.setAskDate(LocalDate.now());
            borrowRepository.save(borrow);
            //Mettre à jour le statut du livre
            bookEntity.setBookStatus(BookStatus.BORROWED);
            bookRepository.save(bookEntity);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/borrows/{borrowId}")
    public ResponseEntity deleteBorrow(@PathVariable("borrowId") String borrowId) {
        Optional<Borrow> borrow = borrowRepository.findById(Integer.valueOf(borrowId));
        if(borrow.isEmpty()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Borrow borrowEntity = borrow.get();
        borrowEntity.setCloseDate(LocalDate.now());
        borrowRepository.save(borrowEntity);
        //Metre à jour book
        Book book = borrowEntity.getBook();
        book.setBookStatus(BookStatus.FREE);
        bookRepository.save(book);
        return new ResponseEntity(HttpStatus.OK);
    }
}
