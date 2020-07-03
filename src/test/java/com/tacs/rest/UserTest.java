package com.tacs.rest;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;
import com.tacs.rest.validator.UserValidator;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional

public class UserTest {
	
	@Autowired
	UserService userService;
	User user = new User();
	
	@Before
	public void initUseCase() {
		user.setFirstName("Prueba");
		user.setLastName("Prueba");
		user.setPassword("Prueba");
		user.setUsername("Prueba");
		userService.save(user);	
	}
	
	@Test
	public void A_createUser() {			
		Assert.assertEquals(userService.findByUsername(user.getUsername()),user);
	}
	
	@Test
	public void B_cannotAcceptSameUser() {
		User user2 = new User();
		user2.setLastName("Pruebita");
		user2.setPassword("Pruebita");
		user2.setUsername("Prueba");
		user2.setFirstName("Pruebita");
		if(UserValidator.registrationValidator(user)) {
			Assert.assertEquals(null, userService.save(user2));
		}
	}
	
	@Test
	public void C_cannotAcceptNullFirstName() {		
		User user2 = new User();
		user2.setLastName("Pruebita");
		user2.setPassword("Pruebita");
		user2.setUsername("Pruebita");
		Assert.assertTrue(UserValidator.registrationValidator(user2));			
	}
	
	@Test
	public void D_cannotAcceptNullLastName() {		
		User user2 = new User();
		user2.setPassword("Pruebita");
		user2.setUsername("Pruebita");
		user2.setFirstName("Pruebita");
		Assert.assertTrue(UserValidator.registrationValidator(user2));			
	}
	
	@Test
	public void E_cannotAcceptNullUsername() {		
		User user2 = new User();
		user2.setLastName("Pruebita");
		user2.setPassword("Pruebita");
		user2.setFirstName("Pruebita");
		Assert.assertTrue(UserValidator.registrationValidator(user2));			
	}
	
	@Test
	public void F_cannotAcceptNullPassword() {		
		User user2 = new User();
		user2.setFirstName("Pruebita");
		user2.setLastName("Pruebita");
		user2.setUsername("Pruebita");
		Assert.assertTrue(UserValidator.registrationValidator(user2));			
	}
	
	@Test
	public void G_ExistsTreeUsers() {
		//Son Rufus, el admin y el userprueba
		Assert.assertEquals(3, userService.findAll().size());
	}
	
	@Test
	public void H_ModifyUserFirstName() {
		User u = userService.findByUsername(user.getUsername());
		u.setFirstName("Test");
		userService.modifyUser(u.getId(), u);		
		Assert.assertEquals(userService.findByUsername(user.getUsername()).getFirstName(), u.getFirstName());
	}
	
	public void I_ModifyUserLastName() {
		User u = userService.findByUsername(user.getUsername());
		u.setLastName("Test");
		userService.modifyUser(u.getId(), u);		
		Assert.assertEquals(userService.findByUsername(user.getUsername()).getLastName(), u.getLastName());

	}
	
	public void K_ModifyUserPassword() {
		User u = userService.findByUsername(user.getUsername());
		u.setPassword("Test");
		userService.modifyUser(u.getId(), u);		
		Assert.assertEquals(userService.findByUsername(user.getUsername()).getPassword(), u.getPassword());

	}
	
}
