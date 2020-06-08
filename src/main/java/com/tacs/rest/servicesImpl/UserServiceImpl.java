package com.tacs.rest.servicesImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        //TODO: Llamar a la BD
//		User user = daoUser.findById(id);

        //MOCK
        List<User> users = (List<User>) RestApplication.data.get("Users");
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean save(User user) {
        String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pw_hash);
        long cantUsers = 0;
        

        	 cantUsers = daoUser.count();
             for(int i = 0; i <(int)cantUsers; i ++) {
             	
             	Optional<User> userTabla = daoUser.findById(i+1);
             	
             	if(userTabla.get().getUsername().equals(user.getUsername())) {
             		return false;
             	}
             }

    	   daoUser.save(user);

		return true;
        
    }

    @Override
    public void deleteById(int id) {
//		daoUser.deleteById(id);
    }

    @Autowired
    PasswordEncoder passwordEncoder;
	@Override
    public boolean registerUser(User user) {
        // TODO Agregar llamada a la BD
    	
        //MOCK
        List<User> users = (List<User>) RestApplication.data.get("Users");
        for (User userBd : users) {
            if (userBd.getUsername().equals(user.getUsername())) {
                return false;
            }
        }

        user.setId(users.size() + 1);
                
        String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pw_hash);
        users.add(user);

        RestApplication.data.put("Users", users);
        return true;
    }

    @Override
    public User findByTelegramId(long telegram_id) {

        //MOCK
        List<User> users = (List<User>) RestApplication.data.get("Users");
        for (User user : users) {
            if (user.getTelegram_chat_id() == telegram_id) {
                return user;
            }
        }
        return null;
    }
    

}