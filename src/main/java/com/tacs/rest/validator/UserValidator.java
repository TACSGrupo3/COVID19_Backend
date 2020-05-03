package com.tacs.rest.validator;

import com.tacs.rest.entity.User;

public class UserValidator {

    public static boolean logInValidator(User user) {
        return user.getPassword() == null || user.getUsername() == null;
    }

    public static boolean registrationValidator(User user) {
        return UserValidator.logInValidator(user) || user.getFirstName() == null || user.getLastName() == null;
    }

    public static boolean logInGoogleValidator(User user) {
        return user == null || user.getUsername() == null;
    }
}








