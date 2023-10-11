package com.sadev.sharebook.borrow;

import com.sadev.sharebook.book.Book;
import com.sadev.sharebook.book.BookController;
import com.sadev.sharebook.book.BookRepository;
import com.sadev.sharebook.book.BookStatus;
import com.sadev.sharebook.schema.BorrowResponse;
import com.sadev.sharebook.schema.UserInfoResponse;
import com.sadev.sharebook.user.User;
import com.sadev.sharebook.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class BorrowController {
    @Autowired
    BorrowRepository borrowRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookController bookController;

    @GetMapping(value = "/borrows")
    public ResponseEntity<?> getMyBorrows(Principal principal) {
        List<Borrow> borrows = borrowRepository.findByBorrowerId(bookController.getUserConnectedId(principal));
        List<BorrowResponse> borrowResponses = new ArrayList<>();
        if (!borrows.isEmpty()) {
            for(Borrow borrow : borrows){
                borrowResponses.add(new BorrowResponse(borrow.getId(),
                        UserInfoResponse.getUserInfoResponse(borrow.getBorrower()),
                        UserInfoResponse.getUserInfoResponse(borrow.getLender()),
                        borrow.getBook().getTitle(), borrow.getBook().getCategory().getLabel(),
                        borrow.getAskDate(), borrow.getCloseDate()));
            }
            return new ResponseEntity(borrowResponses, HttpStatus.OK);
        }
        return new ResponseEntity("Vous n'avez aucun emprunt !",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/borrows/{bookId}")
    public ResponseEntity createBorrow(@PathVariable("bookId") String bookId, Principal principal) {
        Integer userConnectedId = bookController.getUserConnectedId(principal);
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
            return new ResponseEntity("L'emprunt a été effectué avec succès !",HttpStatus.CREATED);
        }
        return new ResponseEntity("L'emprunt n'a pas pu être effectué !",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/borrows/{borrowId}")
    public ResponseEntity deleteBorrow(@PathVariable("borrowId") String borrowId) {
        Optional<Borrow> borrow = borrowRepository.findById(Integer.valueOf(borrowId));
        if(borrow.isEmpty()){
            return new ResponseEntity("Emprunt inexistant!", HttpStatus.BAD_REQUEST);
        }
        Borrow borrowEntity = borrow.get();
        borrowEntity.setCloseDate(LocalDate.now());
        borrowRepository.save(borrowEntity);
        //Metre à jour book
        Book book = borrowEntity.getBook();
        book.setBookStatus(BookStatus.FREE);
        bookRepository.save(book);
        return new ResponseEntity("L'emprunt a été clos avec succès !",HttpStatus.OK);
    }
}
