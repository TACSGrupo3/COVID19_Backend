package com.tacs.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;
import com.tacs.rest.validator.UserValidator;

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;

	@PostMapping("/users")
	public ResponseEntity<User> registration(@Validated @RequestBody User user) {

		try {
			if (UserValidator.registrationValidator(user)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso todos los datos requeridos");
			} else {
				userService.registerUser(user);
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error en el servidor.");
		}

	}
}