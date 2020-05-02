package com.tacs.rest.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;

@Service
@SuppressWarnings("unchecked")
public class UserServiceImpl implements UserService {

//	@Autowired
//	private DaoUser daoUser;
	
	@Override
	public List<User> findAll() {
		//TODO: LLamar a la BD
//		List<User> listUsers= daoUser.findAll();

		//MOCK 
		List<User> users = (List<User>) RestApplication.data.get("Users");
		return users;
	}

	@Override
	public User findById(int id) {
		//TODO: Llamar a la BD
//		User user = daoUser.findById(id);

		//MOCK
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for(User user: users) {
			if(user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	@Override
	public void save(User user) {
//		daoUser.save(user);
	}

	@Override
	public void deleteById(int id) {
//		daoUser.deleteById(id);
	}

	@Override
	public boolean registerUser(User user) {
		// TODO Agregar llamada a la BD
		
		//MOCK
		List<User> users = (List<User>) RestApplication.data.get("Users");
		for(User userBd : users) {
			if(userBd.getUsername().equals(user.getUsername())) {
				return false;
			}
		}
		
		user.setId(users.size()+1);
		users.add(user);
		
		RestApplication.data.put("Users",users);
		return true;
	}

}