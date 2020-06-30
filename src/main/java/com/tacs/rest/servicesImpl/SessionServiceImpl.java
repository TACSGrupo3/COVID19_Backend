package com.tacs.rest.servicesImpl;

import org.springframework.stereotype.Service;

import com.tacs.rest.entity.User;
import com.tacs.rest.services.SessionService;
import com.tacs.rest.services.UserService;

@Service
public class SessionServiceImpl implements SessionService {

    
    private final UserService userService;

    public SessionServiceImpl(UserService userService) {
      this.userService = userService;
    }
    

    @Override
    public User login(User user) {

        return userService.checkUser(user);
    }


    @Override
    public User loginWithSocial(User user) {
        User userRegistered = null;
        userRegistered = this.login(user);

        if (userRegistered != null) return userRegistered;

        //Si llego aca es porque no existe -> Agrego el usuario a la BD

        return userService.save(user);
    }
}
