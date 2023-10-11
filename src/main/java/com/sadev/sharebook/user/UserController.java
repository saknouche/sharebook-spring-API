package com.sadev.sharebook.user;

import com.sadev.sharebook.jwt.JwtController;
import com.sadev.sharebook.jwt.JwtFilter;
import com.sadev.sharebook.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {

        User existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser != null) {
            return new ResponseEntity("User already existing", HttpStatus.BAD_REQUEST);
        }
        User userSaved = saveUser(user);
        return new ResponseEntity("L'inscription a bien été effectuée !", HttpStatus.CREATED);
    }

    @GetMapping(value = "/isConnected")
    public ResponseEntity getUserConnected() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return new ResponseEntity(((UserDetails) principal).getUsername(), HttpStatus.OK);
        }
        System.out.println(((UserDetails) principal).getUsername());
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
