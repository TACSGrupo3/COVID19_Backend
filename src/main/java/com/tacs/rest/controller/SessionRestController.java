package com.tacs.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.validator.UserValidator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RestController

public class SessionRestController {
	static final long TOKEN_DURATION = 600000;
	
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
		user.setPassword("Rufus");
		list.add(user);	
		UserValidator uv = new UserValidator();
		
		if(uv.logInValidator(user2)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso el usuario o la contrasena");
		}
		else if(user.getPassword().equals(user2.getPassword())&& user.getUsername().equals(user2.getUsername())
				|| user2.getPassword().toLowerCase().equals("admin") && user2.getUsername().toLowerCase().equals("admin")){
			user2.setFirstName(user2.getUsername());
			user2.setLastName(user2.getUsername());
			user2.setToken(getJWTToken(user2.getUsername()));
			return new ResponseEntity<User> (user2,HttpStatus.OK);	
		}
		else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La combinación de Usuario y Contraseña es inválida");
		}
	}
	
	@DeleteMapping("/session")
	public ResponseEntity<?> logOut(@RequestBody User user){
	return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	private String getJWTToken(String username) {
		String secretKey = "tacsGrupo3";
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
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_DURATION))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
}
