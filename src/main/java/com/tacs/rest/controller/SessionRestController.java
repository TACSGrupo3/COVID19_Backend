package com.tacs.rest.controller;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.services.SessionService;
import com.tacs.rest.validator.UserValidator;


@RestController

public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @PostMapping("/session")
    public ResponseEntity<User> logIn(@RequestBody User user) {

        if (UserValidator.logInValidator(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso el usuario o la contrasena");
        }

        User userAuthenticated = sessionService.login(user);
        if (userAuthenticated == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La combinación de Usuario y Contraseña es inválida");
        } else {
            userAuthenticated.setToken(generateNewToken());
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
            userAuthenticated.setToken(generateNewToken());
            return new ResponseEntity<User>(userAuthenticated, HttpStatus.OK);
        }
    }

    @DeleteMapping("/session")
    public ResponseEntity<?> logOut() {
        //TODO: Revocar EL token
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
