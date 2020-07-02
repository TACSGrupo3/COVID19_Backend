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
import com.tacs.rest.services.SessionService;
import com.tacs.rest.services.UserService;
import com.tacs.rest.validator.UserValidator;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional

public class SessionTest {
	
	@Autowired 
	UserService userService;
	@Autowired
	SessionService sessionService;
	
	User user = new User();
	User user2 = new User();
	@Before
	public void initUseCase() {
		user.setFirstName("Prueba");
		user.setLastName("Prueba");
		user.setPassword("Prueba");
		user.setUsername("Prueba");
		userService.save(user);	
	}
		
	@Test
	public void A_Login() {
		user2.setUsername("Prueba");
		user2.setPassword("Prueba");
		if(UserValidator.logInValidator(user2)) 
			Assert.assertEquals(user, sessionService.login(user2));
	}
	
	@Test
	public void B_NotExistsUsernameLogin() {
		user2.setUsername("Test");
		user2.setPassword("Prueba3");
		if(UserValidator.logInValidator(user2)) 
			Assert.assertEquals(null, sessionService.login(user2)); 
		
	}
	@Test
	public void C_NotPasswordMatchingLogin() {
		user2.setUsername("Prueba");
		user2.setPassword("Prueba3");
		if(UserValidator.logInValidator(user)) 
		Assert.assertEquals(null, sessionService.login(user2));
	}
	@Test
	public void D_NotUsernameLogin() {
		user2.setPassword("Prueba");
		Assert.assertEquals(true, UserValidator.logInValidator(user2));
	}
	@Test
	public void E_NotPasswordLogin() {
		user2.setUsername("Prueba");
		Assert.assertEquals(true, UserValidator.logInValidator(user2));
	}

		
	
	
	
	
	
	
	
	
	
	
	
	
	
}

	


