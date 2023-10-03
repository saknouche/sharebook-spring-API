package com.sadev.sharebook.user;

import com.sadev.sharebook.jwt.JwtController;
import com.sadev.sharebook.jwt.JwtFilter;
import com.sadev.sharebook.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtController jwtController;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtFilter jwtFilter;

    @PostMapping(value = "/users")
    public ResponseEntity addUser(@Valid @RequestBody User user) {

        User existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser != null) {
            return new ResponseEntity("User already existing", HttpStatus.BAD_REQUEST);
        }
        User userSaved = saveUser(user);
        Authentication authentication = jwtController.logUser(user.getEmail(), user.getPassword());
        String jwt = jwtUtils.generateToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity(userSaved,httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/isConnected")
    public ResponseEntity getUSerConnected() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return new ResponseEntity(((UserDetails) principal).getUsername(), HttpStatus.OK);
        }
        return new ResponseEntity("User is not connected", HttpStatus.FORBIDDEN);
    }

    private User saveUser(User user) {
        User userToSave = new User();
        userToSave.setEmail(user.getEmail());
        userToSave.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userToSave.setFirstName(StringUtils.capitalize(user.getFirstName()));
        userToSave.setLastName(StringUtils.capitalize(user.getLastName()));
        userRepository.save(userToSave);
        return userToSave;
    }
}
