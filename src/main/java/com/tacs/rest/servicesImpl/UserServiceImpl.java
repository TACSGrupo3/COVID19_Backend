package com.tacs.rest.servicesImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.tacs.rest.dao.UserDAO;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO daoUser;

    @Override
    public List<User> findAll() {
    	return (List<User>) daoUser.findAll();

    }

    @Override
    public User findById(int id) {
      
    	return daoUser.findById(id).orElse(null);
    	
    }
    @Override
    public User save(User user) {
        String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pw_hash);

        if (this.findByUsername(user.getUsername())!= null) {     	  
     	   return null;
        }
        daoUser.save(user);
        return user;
    }
    
	@Override
	public void saveAll(List<User> users) {
		users.forEach(user -> daoUser.save(user));	
	}
    
    @Override
    public User findByUsername (String username){
    	if (!daoUser.findByUsername(username).isEmpty()) {
    		return daoUser.findByUsername(username).get(0);
    	}
    	return null;  	
    }

    @Override
    public void deleteById(int id) {
		daoUser.deleteById(id);
    }

    @Override
    public User findByTelegramId(long telegram_id) {
    	if (!daoUser.findByTelegramChatId(telegram_id).isEmpty()) {
    		return daoUser.findByTelegramChatId(telegram_id).get(0);
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
    
    @Override
	public boolean sameNameList(String nameList, int id) {
    	User user  = this.findById(id);
    	List<CountriesList> cl = user.getCountriesList();
    	return cl.stream().anyMatch(n -> n.getName().equals(nameList));
    }
    
	@Override
	public int cantUsers() {
		return (int)daoUser.count();
	}
	@Override //////////////////////////////////////////////////////
	public User userWithCountriesList (int countriesListId) {
		List<User> users = daoUser.findByCountriesList_idCountriesList(countriesListId);
    	if (!users.isEmpty()) {
    		return users.get(0);
    	}
    	return null;
	}
	@Override
	public List<User> userInterestedOnCountry(int idCountry){
		List<User>  usersDB = this.findAll();
		return usersDB.stream().filter(u -> u.hasCountry(idCountry)).collect(Collectors.toList());	
		
	}
	
}