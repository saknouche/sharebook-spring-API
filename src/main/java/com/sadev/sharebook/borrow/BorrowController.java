package com.sadev.sharebook.borrow;

import com.sadev.sharebook.book.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class BorrowController {
    @GetMapping(value = "/borrows")
    public ResponseEntity getMyBorrows(){
        //TODO:jpa
        Borrow borrow = new Borrow();
        borrow.setAskDate(LocalDate.now());
        return new ResponseEntity(borrow, HttpStatus.OK);
    }

    @PostMapping("/borrows/{bookId}")
    public ResponseEntity createBorrow(@PathVariable("bookId") String bookId){
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/borrows/{borrowId}")
    public ResponseEntity deleteBorrow(@PathVariable("borrowId") String borrowId){
        return new ResponseEntity(HttpStatus.OK);
    }
}
