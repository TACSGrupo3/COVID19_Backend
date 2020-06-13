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
	public void saveAll(List<User> users) {
		users.forEach(user -> daoUser.save(user));	
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
	@Override
	public User userWithCountriesList (int countriesListId) {
		
		List<User> users = this.findAll();
		for(int i = 0 ; i < this.cantUsers() ; i++) {
			users.get(i);

			if (users.get(i).getCountriesList().stream().anyMatch(n -> n.getId()==countriesListId)) {
				return users.get(i);
			}
			
		}
		return null;
	}
	@Override
	public List<User> userInterestedOnCountry(int idCountry){
		List<User>  usersDB = this.findAll();
		return usersDB.stream().filter(u -> u.hasCountry(idCountry)).collect(Collectors.toList());	
		
	}
	
}