package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;


@RestController

public class LogInRestController {
	@GetMapping("/login")
	public ResponseEntity<?> login(){		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> loggearlo(@Validated @RequestBody User user2) {
			
		User user = new User();
		List<User> list = new ArrayList<User>();
		user.setId(1);
		user.setUsername("Rufus");
		user.setFirstName("TEST");
		user.setLastName("TEST");
		user.setPassword("Rufus");
		list.add(user);	
		
		if(user.getPassword().equals(user2.getPassword())&& user.getUsername().equals(user2.getUsername())){
			return new ResponseEntity<User> (user2,HttpStatus.OK);	
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o contraseña incorrecta");
		}
	}
	
}
