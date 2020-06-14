package com.tacs.rest.dao;

import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.User;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserDAO extends CrudRepository<User, Integer>  {

	List<User> findByUsername(String username);
	List<User> findByTelegramChatId(long telegram_chat_id);

}
