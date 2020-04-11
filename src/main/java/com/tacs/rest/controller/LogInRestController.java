package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tacs.rest.entity.User;


@RestController

public class LogInRestController {
	@GetMapping("/login")
	public String login(){

		
		return "Ingresaste";
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loggearlo(@RequestBody User user2) {
		
		
		
		User user = new User();
		List<User> list = new ArrayList<User>();
		user.setId(1);
		user.setUsername("Rufus");
		user.setFirstName("TEST");
		user.setLastName("TEST");
		user.setPassword("Rufus");
		list.add(user);	
		
		if(user.getPassword().equals(user2.getPassword())&& user.getUsername().equals(user2.getUsername())){
			return new ResponseEntity<> ("Hola "+ user2.getUsername()+ "!!",HttpStatus.OK);	
		}
		else {
			return new ResponseEntity<> ("Usuario o Contrasenia incorrecta", HttpStatus.BAD_REQUEST);
		}
	}
	
}
