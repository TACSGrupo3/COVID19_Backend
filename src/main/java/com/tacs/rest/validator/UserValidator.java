package com.tacs.rest.validator;

import com.tacs.rest.entity.User;

public class UserValidator {
	
	public boolean logInValidator(User user) {
		return user.getPassword()==null || user.getUsername()==null;
	}
	public boolean registrationValidator(User user) {
		return this.logInValidator(user)||user.getFirstName()==null||user.getLastName()==null;
	}
}








