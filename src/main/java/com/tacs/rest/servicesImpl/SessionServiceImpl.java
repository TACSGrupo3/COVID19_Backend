package com.tacs.rest.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.SessionService;

@Service
@SuppressWarnings("unchecked")
public class SessionServiceImpl implements SessionService{

	
	@Override
	public User login(User user) {
		//TODO: Llamar a la BD
		
		//MOCK
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for(User userBd : users) {
			if(userBd.getUsername().toLowerCase().equals(user.getUsername().toLowerCase()) && 
					userBd.getPassword().toLowerCase().equals(user.getPassword().toLowerCase())) {
				User response = new User(userBd.getId(), userBd.getUsername(), userBd.getFirstName(), 
						userBd.getLastName(), null, userBd.getCountriesList(), userBd.getUserRole());
				return response;
			}
		}
		return null;
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
