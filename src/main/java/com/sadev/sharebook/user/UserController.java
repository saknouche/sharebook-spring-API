package com.sadev.sharebook.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping(value = "/users")
    public ResponseEntity addUser(@Valid @RequestBody User user){
        User user1 = new User("sadev@gmail.com");
        return new ResponseEntity(user1, HttpStatus.CREATED);
    }
}
