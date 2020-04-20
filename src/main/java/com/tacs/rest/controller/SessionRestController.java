package com.tacs.rest.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.validator.UserValidator;


@RestController

public class SessionRestController {
	
	private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

	public static String generateNewToken() {
	    byte[] randomBytes = new byte[24];
	    secureRandom.nextBytes(randomBytes);
	    return base64Encoder.encodeToString(randomBytes);
	}
	
	@GetMapping("/session")
	public ResponseEntity<?> log(){		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping("/session")
	public ResponseEntity<User> logIn(@RequestBody User user2) {
			
		User user = new User();
		List<User> list = new ArrayList<User>();
		user.setId(1);
		user.setUsername("Rufus");
		user.setFirstName("TEST");
		user.setLastName("TEST");
		user.setPassword("Rufus");
		list.add(user);	
		UserValidator uv = new UserValidator();
		
		if(uv.logInValidator(user2)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso el usuario o la contrasena");
		}
		else if(user.getPassword().equals(user2.getPassword())&& user.getUsername().equals(user2.getUsername())
				|| user2.getPassword().toLowerCase().equals("admin") && user2.getUsername().toLowerCase().equals("admin")){
			user2.setToken(generateNewToken());
			return new ResponseEntity<User> (user2,HttpStatus.OK);	
		}
		else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La combinación de Usuario y Contraseña es inválida");
		}
	}
	
	@PostMapping("/sessionWithSocial")
	public ResponseEntity<User> logInWithSocial(@RequestBody User user2) {
			
		User user = new User();
		List<User> list = new ArrayList<User>();
		user.setId(1);
		list.add(user);	
		UserValidator uv = new UserValidator();
		
		if(uv.logInGoogleValidator(user2)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error en la autenticación");
		}
		else{
			//Agregar autenticacion en la BD
			user2.setToken(generateNewToken());
			return new ResponseEntity<User> (user2,HttpStatus.OK);	
		}
	}
	
	@DeleteMapping("/session")
	public ResponseEntity<?> logOut(@RequestBody User user){
	return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
