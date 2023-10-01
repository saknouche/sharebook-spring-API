package com.sadev.sharebook.book;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class BookController {

    @GetMapping(value = "/books")
    public ResponseEntity listBooks(){
        Book book = new Book();
        book.setTitle("MyBook");
        book.setCategory(new Category("BD"));
        return new ResponseEntity(Arrays.asList(book), HttpStatus.OK);
    }
    @PostMapping(value = "/books")
    public ResponseEntity addBook(@Valid @RequestBody Book book){
        //TODO: persist
        return new ResponseEntity(book, HttpStatus.CREATED);
    }

    @PutMapping(value = "/books/{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") String bookId, @RequestBody Book book){
        //TODO: UPDATE
        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping(value = "/books/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") String bookId){
        //TODO: delete
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/categories")
    public ResponseEntity listCategories(){
        Category category = new Category("BD");
        Category categoryRoman = new Category("Roman");
        return new ResponseEntity(Arrays.asList(category, categoryRoman),HttpStatus.OK);
    }
}
