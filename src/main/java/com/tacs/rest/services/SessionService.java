package com.tacs.rest.services;

import com.tacs.rest.entity.User;

public interface SessionService {

	public User login(User user);
	
	public User loginWithSocial(User user);
}
