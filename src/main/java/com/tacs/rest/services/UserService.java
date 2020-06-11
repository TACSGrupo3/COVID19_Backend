package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.User;

public interface UserService {
	
	List<User> findAll();
	
	User findById(int id);
	
	User findByUsername(String username);
	
	boolean save(User user);
	
	void deleteById(int id);
	
	User checkUser (User user);
	
	User findByTelegramId(long telegram_id);
	
	boolean sameNameList(String nameList, int id);
	
	public int cantUsers();
	
	public User userWithCountriesList (int countriesListId);
}