package com.sadev.sharebook.jwt;

import com.sadev.sharebook.schema.UserResponse;
import com.sadev.sharebook.user.User;
import com.sadev.sharebook.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class JwtController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity createAuthToken(@RequestBody JwtRequest jwtRequest, HttpServletResponse response) {
        Authentication authentication = logUser(jwtRequest.getEmail(), jwtRequest.getPassword());
        //2-Une fois authentifié
        //3-Générer le token
        String jwt = jwtUtils.generateToken(authentication);
        String accessToken = "Bearer " + jwt;
        //4-renvoyer le token au client
        //HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        //renvoie un objet authentication (email)
        //Object principal = authentication.getPrincipal();
        UserResponse userResponse = new UserResponse();
        User user = userRepository.findOneByEmail(jwtRequest.getEmail());
        if(user != null){
            userResponse.setEmail(user.getEmail());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setAccessToken(accessToken);
            return new ResponseEntity(userResponse, HttpStatus.OK);
        }
        return new ResponseEntity("Utilisateur inconnu !", HttpStatus.NOT_FOUND);
    }

    public Authentication logUser(String email, String password) {
        //1-Appelle loadUserByUsername pour vérifier en BDD
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
