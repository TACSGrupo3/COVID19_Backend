package com.tacs.rest.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.SessionService;
import com.tacs.rest.services.UserService;

@Service
@SuppressWarnings("unchecked")
public class SessionServiceImpl implements SessionService{

	@Autowired
	UserService userService;
	@Override
	public User login(User user) {
		
		return userService.checkUser(user);
	}
	
	@Override
	public User loginWithSocial(User user) {
		//TODO: Llamar a la BD
		
		//MOCK
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for(User userBd : users) {
			if(userBd.getUsername().equals(user.getUsername()) && userBd.getPassword().equals(user.getPassword())) {
				return userBd;
			}
		}
		
		//Si llegó acá es porque no existe -> Agrego el usuario a la BD
		users.add(user);
		RestApplication.data.put("Users",users);
		
		return user;
	}
}
