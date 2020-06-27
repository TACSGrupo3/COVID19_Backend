package com.tacs.rest.services;

import com.tacs.rest.entity.User;

public interface SessionService {

    User login(User user);

    User loginWithSocial(User user);
}
