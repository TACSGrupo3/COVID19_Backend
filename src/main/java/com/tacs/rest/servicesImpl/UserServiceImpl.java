package com.tacs.rest.servicesImpl;

import com.tacs.rest.dao.UserDAO;
import com.tacs.rest.entity.CountriesList;
import com.tacs.rest.entity.User;
import com.tacs.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO daoUser;

    public UserServiceImpl(UserDAO daoUser) {
      this.daoUser = daoUser;
    }
        
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

        if (this.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setUserRole("USER");
        user.setLastAccess(new Date());
        user.setUsername(user.getUsername().toLowerCase());
        daoUser.save(user);
        return user;
    }

    @Override
    public void saveAll(List<User> users) {
        users.forEach(user -> daoUser.save(user));
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = daoUser.findByUsername(username.toLowerCase());
        if (!users.isEmpty()) {
            return users.get(0);
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
    public User checkUser(User user) {
        User userBD = this.findByUsername(user.getUsername());

        if (userBD != null && BCrypt.checkpw(user.getPassword(), userBD.getPassword())) {
            
        	User newUser = new User();
        	
        	newUser.setCountriesList(userBD.getCountriesList());
        	newUser.setFirstName(userBD.getFirstName());
        	newUser.setId(userBD.getId());
        	newUser.setLastAccess(userBD.getLastAccess());
        	newUser.setLastName(userBD.getLastName());
        	newUser.setPassword(userBD.getPassword());
        	newUser.setTelegram_chat_id(userBD.getTelegram_chat_id());
        	newUser.setToken(userBD.getToken());
        	newUser.setUsername(userBD.getUsername());
        	newUser.setUserRole(userBD.getUserRole());        	
        	userBD.setLastAccess(new Date());
        	daoUser.save(userBD);
            return newUser;
        }
        return null;
    }

    @Override
    public boolean sameNameList(String nameList, int id) {
        User user = this.findById(id);
        List<CountriesList> cl = user.getCountriesList();
        return cl.stream().anyMatch(n -> n.getName().equals(nameList));
    }

    @Override
    public int cantUsers() {
        return (int) daoUser.count();
    }

    @Override
    public User userWithCountriesList(int countriesListId) {
        List<User> users = daoUser.findByCountriesList_idCountriesList(countriesListId);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public List<User> userInterestedOnCountry(int idCountry) {
        List<User> usersDB = this.findAll();
        return usersDB.stream().filter(u -> u.hasCountry(idCountry)).collect(Collectors.toList());

    }

    @Override
    public Page<User> findAllPageable(Pageable pageable) {
        return this.daoUser.findAll(pageable);
    }

    @Override
    public Page<User> findByFilterPageable(Pageable pageable, String filter) {
        return this.daoUser.fitlerUsersByString(filter.toUpperCase(), pageable);
    }

    @Override
    public User modifyUser(Integer userID, User user) {
        User userBd = this.daoUser.findById(userID).orElse(null);
        if (userBd == null) return null;

        if (!userBd.getFirstName().equals(user.getFirstName()) && isNotNullOrEmpty(user.getFirstName()))
            userBd.setFirstName(user.getFirstName());

        if (!userBd.getLastName().equals(user.getLastName()) && isNotNullOrEmpty(user.getLastName()))
            userBd.setLastName(user.getLastName());

        if (isNotNullOrEmpty(user.getPassword())) {
            if (!BCrypt.checkpw(user.getPassword(), userBd.getPassword()))
                userBd.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        if (userBd.getTelegram_chat_id() != user.getTelegram_chat_id())
            userBd.setTelegram_chat_id(user.getTelegram_chat_id());

        return this.daoUser.save(userBd);
    }

    private boolean isNotNullOrEmpty(String string) {
        if (string != null && !string.equals(""))
            return true;
        else
            return false;
    }

    public User findUserAndListsByTelegramChatId(long chat_id) {
        return this.daoUser.findUserAndListsByTelegramChatId(chat_id);
    }

}