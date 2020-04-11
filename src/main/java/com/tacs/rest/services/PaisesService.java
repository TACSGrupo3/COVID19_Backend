package com.tacs.rest.services;

import java.util.List;

import com.tacs.rest.entity.User;

public interface PaisesService {
	
	public List<User> findAll();
	
	public User findById(int id);
	
	public void save(User user);
	
	public void deleteById(int id);
}