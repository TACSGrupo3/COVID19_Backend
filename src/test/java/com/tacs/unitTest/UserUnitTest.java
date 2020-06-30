package com.tacs.unitTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.tacs.rest.dao.UserDAO;
import com.tacs.rest.entity.User;
import com.tacs.rest.servicesImpl.SessionServiceImpl;
import com.tacs.rest.servicesImpl.UserServiceImpl;

import static org.mockito.AdditionalAnswers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUnitTest {

	  @Mock
	  private UserDAO userRepository = Mockito.mock(UserDAO.class);;

	  private UserServiceImpl userService;
	  private SessionServiceImpl sessionService;
	  User user;

	  @BeforeEach
	  void initUseCase() {
		  userService = new UserServiceImpl(userRepository); 
		  sessionService = new SessionServiceImpl(userService);
		  user = new User (1, "rufus", "rufus", "Carmaicol", "rufus", "USER");
		  
	  }
	  
	  @Test
	  void saveUser() {
		  when(userRepository.save(any(User.class))).then(returnsFirstArg());
		  User savedUser = userService.save(user);
		  Assert.assertEquals(savedUser.getUsername(), "rufus");
	  }


	
	
}
