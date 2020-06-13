package com.tacs.rest.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
		User userRegistered = null;
		userRegistered = this.login(user);
		
		if(userRegistered != null) return userRegistered;
		
		//Si llegó acá es porque no existe -> Agrego el usuario a la BD
		List<User> users = (List<User>) RestApplication.data.get("Users");
		user.setId(users.size() + 1);
		String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
	    user.setPassword(pw_hash);
		users.add(user);
		RestApplication.data.put("Users",users);
		
		return user;
	}
}
