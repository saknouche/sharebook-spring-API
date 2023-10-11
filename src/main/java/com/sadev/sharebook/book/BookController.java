package com.sadev.sharebook.book;

import com.sadev.sharebook.borrow.Borrow;
import com.sadev.sharebook.borrow.BorrowRepository;
import com.sadev.sharebook.schema.BookResponse;
import com.sadev.sharebook.schema.UserInfoResponse;
import com.sadev.sharebook.user.User;
import com.sadev.sharebook.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BorrowRepository borrowRepository;

    @GetMapping(value = "/books")
    public ResponseEntity<?> listBooks(@RequestParam(required = false) BookStatus status, Principal principal) {

        Integer userConnectedId = this.getUserConnectedId(principal);
        List<Book> books;
        List<BookResponse> bookResponses = new ArrayList<>();
        //freeBooks
        if (status != null && status == BookStatus.FREE) {
            books = bookRepository.findByStatusAndUserIdNotAndDeletedFalse(status, userConnectedId);
            for (Book book : books) {
                bookResponses.add(new BookResponse(book.getId(), book.getTitle(), book.getCategory(),book.getBookStatus(), UserInfoResponse.getUserInfoResponse(book.getUser())));
            }
        } else {
            //Books
            books = bookRepository.findByUserIdAndDeletedFalse(userConnectedId);
            for (Book book : books) {
                bookResponses.add(new BookResponse(book.getId(), book.getTitle(), book.getCategory(), book.getBookStatus()));
            }
        }
        return new ResponseEntity(bookResponses, HttpStatus.OK);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity loadBook(@PathVariable("bookId") String bookId) {
        Optional<Book> optBook = bookRepository.findById(Integer.valueOf(bookId));
        if (!optBook.isPresent()) {
            return new ResponseEntity("Book not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(optBook.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/books")
    public ResponseEntity addBook(@Valid @RequestBody Book book, Principal principal) {
        Integer userConnectedId = this.getUserConnectedId(principal);
        Optional<User> user = userRepository.findById(userConnectedId);
        Optional<Category> category = categoryRepository.findById(book.getCategoryId());
        if (category.isPresent()) {
            book.setCategory(category.get());
        } else {
            return new ResponseEntity("You must provide a valid catgeory", HttpStatus.BAD_REQUEST);
        }
        if (user.isPresent()) {
            book.setUser(user.get());
        } else {
            return new ResponseEntity("You must provide a valid user", HttpStatus.BAD_REQUEST);
        }
        book.setDeleted(false);
        book.setBookStatus(BookStatus.FREE);
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity(savedBook, HttpStatus.CREATED);
    }

    @PutMapping(value = "/books/{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") String bookId, @Valid @RequestBody Book book) {
        Optional<Book> bookToUpdate = bookRepository.findById(Integer.valueOf(bookId));
        if (!bookToUpdate.isPresent()) {
            return new ResponseEntity("Ce livre n'existe pas !", HttpStatus.BAD_REQUEST);
        }
        List<Borrow> borrows = borrowRepository.findByBookId(Integer.valueOf(bookId));
        for(Borrow borrow : borrows){
            if(borrow.getCloseDate() == null){
                return new ResponseEntity("Ce livre est en cours d'emprunt !", HttpStatus.BAD_REQUEST);
            }
        }
        Book bookToSave = bookToUpdate.get();
        Optional<Category> newCategory = categoryRepository.findById(book.getCategoryId());
        bookToSave.setCategory(newCategory.get());
        bookToSave.setTitle(book.getTitle());
        bookRepository.save(bookToSave);
        return new ResponseEntity(bookToSave, HttpStatus.OK);
    }

    @DeleteMapping(value = "/books/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") String bookId) {
        Optional<Book> bookToDelete = bookRepository.findById(Integer.valueOf(bookId));
        if (!bookToDelete.isPresent()) {
            return new ResponseEntity("Ce livre n'existe pas !", HttpStatus.BAD_REQUEST);
        }
        //Vérifier les emprunts en cours de ce livre
        Book book = bookToDelete.get();
        List<Borrow> borrows = borrowRepository.findByBookId(book.getId());
        for (Borrow borrow : borrows) {
            if (borrow.getCloseDate() == null) {
                //User borrower = borrow.getBorrower();
                return new ResponseEntity("Ce livre est en cours d'emprunt !", HttpStatus.CONFLICT);
            }
        }
        //Il est préférable de supprimer le book logiquement avec un boolean que de le supprimer physiquement avec remove
        book.setDeleted(true);
        bookRepository.save(book);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/categories")
    public ResponseEntity listCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        if (categories == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(categories, HttpStatus.OK);
    }

    public Integer getUserConnectedId(Principal principal) {
        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
            throw new RuntimeException("User not found");
        }
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        User oneByEmail = userRepository.findOneByEmail(user.getName());
        return oneByEmail.getId();
    }
}
