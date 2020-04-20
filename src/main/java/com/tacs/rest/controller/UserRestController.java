package com.tacs.rest.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tacs.rest.entity.User;
import com.tacs.rest.validator.UserValidator;

//Indiciamos que es un controlador rest
@RestController
public class UserRestController {
	
	//Inyectamos el servicio para poder hacer uso de el
//	@Autowired
//	private UserService userService;

	/*Este método se hará cuando por una petición GET (como indica la anotación) se llame a la url 
	  URL: http://127.0.0.1:8080/userWS/users  
	*/
	@GetMapping("/users")
	public List<User> findAll(){
		//retornará todos los usuarios
//		return userService.findAll();
		
		//MOCK
		User user = new User();
		List<User> list = new ArrayList<User>();
		user.setId(1);
		user.setFirstName("TEST");
		user.setLastName("TEST");
		
		list.add(user);
		return list;
	}
	
	/*Este método se hará cuando por una petición GET (como indica la anotación) se llame a la url + el id de un usuario
	  URL: http://127.0.0.1:8080/user/1
	*/
	@GetMapping("/users/{userId}")
	public User getUser(@PathVariable int userId){
//		User user = userService.findById(userId);
//		
//		if(user == null) {
//			throw new RuntimeException("User id not found -"+userId);
//		}
//		//retornará al usuario con id pasado en la url
//		return user;
		
//		MOCK
		User user = new User();
		user.setId(userId);
		user.setFirstName("TEST");
		user.setLastName("TEST");
		
		return user;
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> registration(@Validated @RequestBody User user2) {
			
		UserValidator uv = new UserValidator();
		
		if(uv.registrationValidator(user2)){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ingreso todos los datos requeridos");
		}
		else {
			return new ResponseEntity<User> (user2,HttpStatus.OK);
		}
	}

	@PutMapping("/users")
	public User putUser(@RequestBody User user) {
		
//		userService.save(user);
		
		//este metodo actualizará al usuario enviado
		
		return user;
	}

	@PatchMapping("/users")
	public User patchUser(@RequestBody User user) {
		
//		userService.save(user);
		
		//este metodo actualizará al usuario enviado
		
		return user;
	}
	
	/*Este método se hará cuando por una petición DELETE (como indica la anotación) se llame a la url + id del usuario
	  URL: http://127.0.0.1:8080/userWS/users/1  
	*/
	@DeleteMapping("users/{userId}")
	public String deleteUser(@PathVariable int userId) {
		
//		User user = userService.findById(userId);
//		
//		if(user == null) {
//			throw new RuntimeException("User id not found -"+userId);
//		}
//		
//		userService.deleteById(userId);
		
		//Esto método, recibira el id de un usuario por URL y se borrará de la bd.
		return "Deleted user id - "+userId;
	}
	
}