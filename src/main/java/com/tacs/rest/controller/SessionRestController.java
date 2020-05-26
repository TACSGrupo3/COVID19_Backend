package com.tacs.rest.controller;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.services.SessionService;
import com.tacs.rest.validator.UserValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController

public class SessionRestController {
	static final long TOKEN_DURATION = 600000;

    @Autowired
    private SessionService sessionService;

    private String generateNewToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

    
    @PostMapping("/session")
    public ResponseEntity<User> logIn(@RequestBody User user) {

        if (UserValidator.logInValidator(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso el Usuario o la Contraseña");
        }

        User userAuthenticated = sessionService.login(user);
        if (userAuthenticated == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La combinación de Usuario y Contraseña es inválida");
        } else {
            userAuthenticated.setToken(generateNewToken(user.getUsername()));
            return new ResponseEntity<User>(userAuthenticated, HttpStatus.OK);
        }
    }

    @PostMapping("/sessionWithSocial")
    public ResponseEntity<User> logInWithSocial(@RequestBody User user) {

        if (UserValidator.logInGoogleValidator(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error en la autenticación");
        } else {
            User userAuthenticated = sessionService.loginWithSocial(user);
            //Agregar autenticacion en la BD
            userAuthenticated.setToken(generateNewToken(user.getUsername()));
            return new ResponseEntity<User>(userAuthenticated, HttpStatus.OK);
        }
    }

    @DeleteMapping("/session")
    public ResponseEntity<?> logOut(User user) {
        user.setToken(null);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
