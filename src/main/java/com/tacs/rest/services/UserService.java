package com.tacs.rest.services;

import com.tacs.rest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int id);

    User findByUsername(String username);

    User save(User user);

    void deleteById(int id);

    User checkUser(User user);

    User findByTelegramId(long telegram_id);

    boolean sameNameList(String nameList, int id);

    public int cantUsers();

    public User userWithCountriesList(int countriesListId);

    public List<User> userInterestedOnCountry(int idCountry);

    void saveAll(List<User> users);

    Page<User> findAllPageable(org.springframework.data.domain.Pageable pageable);

    Page<User> findByFilterPageable(Pageable pageable, String filter);

    User modifyUser(Integer userID, User user);
}