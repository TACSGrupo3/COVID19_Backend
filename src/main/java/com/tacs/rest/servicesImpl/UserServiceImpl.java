package com.tacs.rest.servicesImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.tacs.rest.RestApplication;
import com.tacs.rest.dao.UserDAO;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;

@Service
@SuppressWarnings("unchecked")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO daoUser;

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
      
    	return daoUser.findById(id).orElse(null);
    	
    }
    @Override
    public boolean save(User user) {
        String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pw_hash);

        if (this.findByUsername(user.getUsername())!= null) {     	  
     	   return false;
        }
        daoUser.save(user);
        return true;
    }
    
    @Override
    public User findByUsername (String username){
    	long cantUsers = 0;
    	cantUsers = daoUser.count();
    	for (int i = 0 ; i <(int) cantUsers; i ++) {
    		User userTabla = this.findById(i+1);
         	if(userTabla.getUsername().equals(username)) {
         		return userTabla;
         	}
    	}
    	return null;
   	
    }

    @Override
    public void deleteById(int id) {
		daoUser.deleteById(id);
    }


    @Override
    public User findByTelegramId(long telegram_id) {
    	long cantUsers = 0;
    	cantUsers = daoUser.count();
    	for (int i = 0 ; i <(int) cantUsers; i ++) {
    		User userTabla = this.findById(i+1);
         	if(userTabla.getTelegram_chat_id()==telegram_id) {
         		return userTabla;
         	}
    		
    	}
    	return null;
    }
    
    @Override 
    public User checkUser (User user) {
    	User userBD = this.findByUsername(user.getUsername());  	
    	
    	if (userBD != null && BCrypt.checkpw(user.getPassword(), userBD.getPassword())) {
    		userBD.setLastAccess(new Date());
    		return userBD;
    	}
    	return null;
    }
    

}