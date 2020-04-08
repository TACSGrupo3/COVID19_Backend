package com.tacs.rest.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;

@Service
public class UserServiceImpl implements UserService {

//	@Autowired
//	private DaoUser daoUser;
	
	@Override
	public List<User> findAll() {
//		List<User> listUsers= daoUser.findAll();
//		return listUsers;
		return null;
	}

	@Override
	public User findById(int id) {
//		User user = daoUser.findById(id);
//		return user;
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

}