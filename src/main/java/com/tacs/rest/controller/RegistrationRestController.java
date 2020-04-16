package com.tacs.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.validator.UserValidator;

@RestController
public class RegistrationRestController {
	@GetMapping("/registration")
	public ResponseEntity<?> registration(){		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	@PostMapping("/registration")
	public ResponseEntity<User> registration(@Validated @RequestBody User user2) {
			
		UserValidator uv = new UserValidator();
		
		if(uv.registrationValidator(user2)){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso todos los datos requeridos");
		}
		else {
			return new ResponseEntity<User> (user2,HttpStatus.OK);
		}
	}
	
}
