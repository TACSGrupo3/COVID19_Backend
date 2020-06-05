package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.User;

public interface UserService {
	
	List<User> findAll();
	
	User findById(int id);
	
	boolean save(User user);
	
	void deleteById(int id);
	
	boolean registerUser(User user);

	User findByTelegramId(long telegram_id);
}